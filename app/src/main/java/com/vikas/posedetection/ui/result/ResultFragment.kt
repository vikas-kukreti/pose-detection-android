package com.vikas.posedetection.ui.result

import android.graphics.PointF
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.common.util.JsonUtils
import com.google.gson.Gson
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import com.vikas.posedetection.R
import com.vikas.posedetection.databinding.ResultFragmentBinding
import org.json.JSONObject
import kotlin.math.atan2

class ResultFragment : Fragment() {

    companion object {
        fun newInstance() = ResultFragment()
    }

    private lateinit var viewModel: ResultViewModel
    private lateinit var binding: ResultFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ResultFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ResultViewModel::class.java)
        viewModel.pose.observe(viewLifecycleOwner, {pose ->
            if(pose == null) return@observe
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

            val leftArmAngle: Double = getAngle(leftShoulder, leftElbow, leftWrist)
            val rightArmAngle: Double = getAngle(rightShoulder, rightElbow, rightWrist)
            val leftLegAngle: Double = getAngle(leftHip, leftKnee, leftAnkle)
            val rightLegAngle: Double = getAngle(rightHip, rightKnee, rightAnkle)

            var string = ""
            val gson = Gson()


            string += "leftShoulder(A): " + gson.toJson(leftShoulder.position, PointF::class.java) + "\n"
            string += "rightShoulder(B): " + gson.toJson(rightShoulder.position, PointF::class.java) + "\n\n"

            string += "leftElbow(C): " + gson.toJson(leftElbow.position, PointF::class.java) + "\n"
            string += "rightElbow(D): " + gson.toJson(rightElbow.position, PointF::class.java) + "\n\n"

            string += "leftWrist(E): " + gson.toJson(leftWrist.position, PointF::class.java) + "\n"
            string += "rightWrist(F): " + gson.toJson(rightWrist.position, PointF::class.java) + "\n\n"

            string += "leftHip(G): " + gson.toJson(leftHip.position, PointF::class.java) + "\n"
            string += "rightHip(H): " + gson.toJson(rightHip.position, PointF::class.java) + "\n\n"

            string += "leftKnee(K): " + gson.toJson(leftKnee.position, PointF::class.java) + "\n"
            string += "rightKnee(L): " + gson.toJson(rightKnee.position, PointF::class.java) + "\n\n"

            string += "leftAnkle(I): " + gson.toJson(leftAnkle.position, PointF::class.java) + "\n"
            string += "rightAnkle(J): " + gson.toJson(rightAnkle.position, PointF::class.java) + "\n\n"

            string += "leftArmAngle(C): $leftArmAngle\n"
            string += "rightArmAngle(D): $rightArmAngle\n"
            string += "leftLegAngle(I): $leftLegAngle\n"
            string += "rightLegAngle(J): $rightLegAngle\n"

            binding.editText.setText(string)
        })
        viewModel.poseBitmap.observe(viewLifecycleOwner, {
            if(it == null) return@observe
            binding.imageView.setImageBitmap(it)
        })
    }
    private fun getAngle(
        firstPoint: PoseLandmark,
        midPoint: PoseLandmark,
        lastPoint: PoseLandmark
    ): Double {
        var result = Math.toDegrees(
            atan2(
                (lastPoint.position.y - midPoint.position.y).toDouble(), (
                        lastPoint.position.x - midPoint.position.x).toDouble()
            )
                    - atan2(
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


}