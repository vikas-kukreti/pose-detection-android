package com.vikas.posedetection.ui.camera

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import com.vikas.posedetection.R
import com.vikas.posedetection.databinding.CameraFragmentBinding


class CameraFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = CameraFragment()
    }

    private lateinit var binding: CameraFragmentBinding
    private lateinit var viewModel: CameraViewModel
    private lateinit var poseDetector: PoseDetector
    private lateinit var bitmap: Bitmap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CameraFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CameraViewModel::class.java)
        val options = AccuratePoseDetectorOptions.Builder()
            .setDetectorMode(AccuratePoseDetectorOptions.SINGLE_IMAGE_MODE)
            .build()
        poseDetector = PoseDetection.getClient(options)
        binding.camera.setLifecycleOwner(viewLifecycleOwner)
        binding.camera.addCameraListener(object: CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                super.onPictureTaken(result)
                result.toBitmap {
                    if(it != null) {
                        bitmap = it
                        detectPose()
                    }
                }
            }
        })

        binding.button.setOnClickListener {
            binding.camera.takePicture()
        }
//        binding.camera.addFrameProcessor {
//            val image = it.getData<Image>()
//            detectPose()
//        }
    }

    private fun detectPose() {
        binding.progress.visibility = View.VISIBLE
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        poseDetector.process(inputImage).addOnSuccessListener {
            processPose(it)
            binding.progress.visibility = View.INVISIBLE
        }.addOnFailureListener {
            binding.progress.visibility = View.INVISIBLE
        }
    }

    private fun processPose(pose: Pose) {
        try {

            val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
            val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)

            val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
            val rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)

            val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
            val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)

            val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
            val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)

            val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
            val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)

            val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
            val rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)

            val leftShoulderP = leftShoulder.position
            val lShoulderX = leftShoulderP.x
            val lShoulderY = leftShoulderP.y
            val rightSoulderP = rightShoulder.position
            val rShoulderX = rightSoulderP.x
            val rShoulderY = rightSoulderP.y

            val leftElbowP = leftElbow.position
            val lElbowX = leftElbowP.x
            val lElbowY = leftElbowP.y
            val rightElbowP = rightElbow.position
            val rElbowX = rightElbowP.x
            val rElbowY = rightElbowP.y

            val leftWristP = leftWrist.position
            val lWristX = leftWristP.x
            val lWristY = leftWristP.y
            val rightWristP = rightWrist.position
            val rWristX = rightWristP.x
            val rWristY = rightWristP.y

            val leftHipP = leftHip.position
            val lHipX = leftHipP.x
            val lHipY = leftHipP.y
            val rightHipP = rightHip.position
            val rHipX = rightHipP.x
            val rHipY = rightHipP.y

            val leftKneeP = leftKnee.position
            val lKneeX = leftKneeP.x
            val lKneeY = leftKneeP.y
            val rightKneeP = rightKnee.position
            val rKneeX = rightKneeP.x
            val rKneeY = rightKneeP.y

            val leftAnkleP = leftAnkle.position
            val lAnkleX = leftAnkleP.x
            val lAnkleY = leftAnkleP.y
            val rightAnkleP = rightAnkle.position
            val rAnkleX = rightAnkleP.x
            val rAnkleY = rightAnkleP.y

            val leftArmAngle: Double = getAngle(leftShoulder, leftElbow, leftWrist)
            val rightArmAngle: Double = getAngle(rightShoulder, rightElbow, rightWrist)
            val leftLegAngle: Double = getAngle(leftHip, leftKnee, leftAnkle)
            val rightLegAngle: Double = getAngle(rightHip, rightKnee, rightAnkle)

            displayAll(
                lShoulderX, lShoulderY, rShoulderX, rShoulderY,
                lElbowX, lElbowY, rElbowX, rElbowY,
                lWristX, lWristY, rWristX, rWristY,
                lHipX, lHipY, rHipX, rHipY,
                lKneeX, lKneeY, rKneeX, rKneeY,
                lAnkleX, lAnkleY, rAnkleX, rAnkleY
            )

            viewModel.setPoseData(pose)
            findNavController().navigate(R.id.action_cameraFragment_to_resultFragment)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Pose Detection Failure", Toast.LENGTH_SHORT).show()
        }
    }

    fun getAngle(
        firstPoint: PoseLandmark,
        midPoint: PoseLandmark,
        lastPoint: PoseLandmark
    ): Double {
        var result = Math.toDegrees(
            Math.atan2(
                (lastPoint.position.y - midPoint.position.y).toDouble(), (
                        lastPoint.position.x - midPoint.position.x).toDouble()
            )
                    - Math.atan2(
                (firstPoint.position.y - midPoint.position.y).toDouble(), (
                        firstPoint.position.x - midPoint.position.x).toDouble()
            )
        )
        result = Math.abs(result) // Angle should never be negative
        if (result > 180) {
            result = (360.0 - result) // Always get the acute representation of the angle
        }
        return result
    }

    private fun displayAll(
        lShoulderX: Float, lShoulderY: Float, rShoulderX: Float, rShoulderY: Float,
        lElbowX: Float, lElbowY: Float, rElbowX: Float, rElbowY: Float,
        lWristX: Float, lWristY: Float, rWristX: Float, rWristY: Float,
        lHipX: Float, lHipY: Float, rHipX: Float, rHipY: Float,
        lKneeX: Float, lKneeY: Float, rKneeX: Float, rKneeY: Float,
        lAnkleX: Float, lAnkleY: Float, rAnkleX: Float, rAnkleY: Float
    ) {
        val paint = Paint()
        paint.color = Color.GREEN
        val strokeWidth = 8.0f
        paint.strokeWidth = strokeWidth
        val drawBitmap = Bitmap.createBitmap(
            bitmap.getWidth(),
            bitmap.getHeight(),
            bitmap.getConfig()
        )
        val canvas = Canvas(drawBitmap)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        canvas.drawLine(lShoulderX, lShoulderY, rShoulderX, rShoulderY, paint)
        canvas.drawLine(rShoulderX, rShoulderY, rElbowX, rElbowY, paint)
        canvas.drawLine(rElbowX, rElbowY, rWristX, rWristY, paint)
        canvas.drawLine(lShoulderX, lShoulderY, lElbowX, lElbowY, paint)
        canvas.drawLine(lElbowX, lElbowY, lWristX, lWristY, paint)
        canvas.drawLine(rShoulderX, rShoulderY, rHipX, rHipY, paint)
        canvas.drawLine(lShoulderX, lShoulderY, lHipX, lHipY, paint)
        canvas.drawLine(lHipX, lHipY, rHipX, rHipY, paint)
        canvas.drawLine(rHipX, rHipY, rKneeX, rKneeY, paint)
        canvas.drawLine(lHipX, lHipY, lKneeX, lKneeY, paint)
        canvas.drawLine(rKneeX, rKneeY, rAnkleX, rAnkleY, paint)
        canvas.drawLine(lKneeX, lKneeY, lAnkleX, lAnkleY, paint)
        paint.color = Color.RED
        paint.textSize = (drawBitmap.width / 20).toFloat()
        canvas.drawText("A", lShoulderX, lShoulderY, paint)
        canvas.drawText("B", rShoulderX, rShoulderY, paint)
        canvas.drawText("C", lElbowX, lElbowY, paint)
        canvas.drawText("D", rElbowX, rElbowY, paint)
        canvas.drawText("E", lWristX, lWristY, paint)
        canvas.drawText("F", rWristX, rWristY, paint)
        canvas.drawText("G", lHipX, lHipY, paint)
        canvas.drawText("H", rHipX, rHipY, paint)
        canvas.drawText("I", lAnkleX, lAnkleY, paint)
        canvas.drawText("J", rAnkleX, rAnkleY, paint)
        canvas.drawText("K", lKneeX, lKneeY, paint)
        canvas.drawText("L", rKneeX, rKneeY, paint)

        viewModel.setPoseBitmap(drawBitmap)

    }

}