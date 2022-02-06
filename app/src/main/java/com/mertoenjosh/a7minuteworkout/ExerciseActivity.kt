package com.mertoenjosh.a7minuteworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var llRestView: LinearLayout
    private lateinit var llExerciseView: LinearLayout
    private lateinit var toolbar_exercise_activity: Toolbar
    private lateinit var progressBar: ProgressBar
    private lateinit var progressBarEx: ProgressBar
    private lateinit var ivExerciseImage: ImageView
    private lateinit var tvExerciseName: TextView
    private lateinit var tvTimer: TextView
    private lateinit var tvTimerEx: TextView
    private lateinit var tvUpcoming: TextView
    private lateinit var rvExerciseStatus: RecyclerView

    private var restTimer: CountDownTimer? = null
    private var exerciseTimer: CountDownTimer? = null
    private var restProgress = 0
    private var exerciseProgress = 0
    private var restTimerDuration = 10
    private var exerciseTimerDuration = 30

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null

    private var exerciseAdapter: ExerciceStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        llRestView = findViewById(R.id.llRestView)
        llExerciseView = findViewById(R.id.llExerciseView)
        toolbar_exercise_activity = findViewById(R.id.toolbar_exercise_activity)
        progressBar = findViewById(R.id.progressBar)
        progressBarEx = findViewById(R.id.progressBarEx)
        ivExerciseImage = findViewById(R.id.ivExerciseImage)
        tvExerciseName = findViewById(R.id.tvExerciseName)
        tvTimer = findViewById(R.id.tvTimer)
        tvTimerEx = findViewById(R.id.tvTimerEx)
        tvUpcoming = findViewById(R.id.tvUpcoming)
        rvExerciseStatus = findViewById(R.id.rvExerciseStatus)

        setSupportActionBar(toolbar_exercise_activity)
        val actionbar = supportActionBar
        actionbar?.title = "EXERCISES"

        actionbar?.setDisplayHomeAsUpEnabled(true)

        toolbar_exercise_activity.setNavigationOnClickListener {
            customDialogForBackButton()
        }
        progressBar.max = restTimerDuration
        progressBarEx.max = exerciseTimerDuration

        exerciseList = Constants.defaultExerciseList()
        tts = TextToSpeech(this, this)

        setupRestView()

        setUpExerciseStatusRecyclerView()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The specified language is not specified!")
            }
        } else {
            Log.e("TTS", "Initialization Failed!")
        }
    }

    override fun onDestroy() {
//        restTimer?.cancel()
//        exerciseTimer?.cancel()

        if (restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }

        if (exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        if (tts != null) {
            tts?.stop()
            tts?.shutdown()
        }

        if (player != null) player?.stop()

        super.onDestroy()
    }

    private fun speakOut(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    private fun setRestProgressBar() {
        restTimer = object : CountDownTimer(restTimerDuration * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progressBar.progress = restTimerDuration - restProgress
                // tvTimer.text = "${restTimerDuration - restProgress}"
                tvTimer.text = "${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                currentExercisePosition++
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
                setupExerciseView()
            }
        }.start()
    }

    private fun setupRestView() {

        try {
            // You can pass a soundURI in place of the sound
            // val soundURI = Uri.parse("android:resource://com.mertoenjosh.a7minuteworkout" + R.raw.press_start)

            player = MediaPlayer.create(applicationContext, R.raw.press_start)
            player?.isLooping = false
            player?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (restTimer != null) {
            restTimer!!.cancel()
            restProgress = 0
        }
        llExerciseView.visibility = View.GONE
        llRestView.visibility = View.VISIBLE
        setRestProgressBar()
        if (currentExercisePosition < exerciseList!!.size - 1){
            tvUpcoming.text = exerciseList!![currentExercisePosition + 1].getName()
        }
    }

    private fun setExerciseProgress() {
        exerciseTimer = object : CountDownTimer(exerciseTimerDuration * 1000L, 1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                progressBarEx.progress = exerciseTimerDuration - exerciseProgress
                tvTimerEx.text = "${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                //
                if (currentExercisePosition <= exerciseList!!.size - 1) {
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                }

                if (currentExercisePosition < exerciseList!!.size - 1) {
                    setupRestView()
                } else {
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                    // Toast.makeText(this@ExerciseActivity, "Well done commodore", Toast.LENGTH_SHORT).show()
                }
            }

        }.start()
    }

    private fun setupExerciseView() {
        llRestView.visibility = View.GONE
        llExerciseView.visibility = View.VISIBLE

        if (exerciseTimer != null) {
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }

        speakOut(exerciseList!![currentExercisePosition].getName())
        setExerciseProgress()
        ivExerciseImage.setImageResource(exerciseList!![currentExercisePosition].getImage())
        tvExerciseName.text = exerciseList!![currentExercisePosition].getName()
    }

    private fun setUpExerciseStatusRecyclerView() {

        // 1. initialize the adapter
        exerciseAdapter = ExerciceStatusAdapter(this, exerciseList!!)

        // 2. set layout manager for the RecyclerView - Horizontal
        rvExerciseStatus.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // 3. Set the RV to use the prepared adapter
        rvExerciseStatus.adapter = exerciseAdapter
    }

    private fun customDialogForBackButton() {
        val customDialog = Dialog(this)

        customDialog.setContentView(R.layout.dialog_custom_back_confirmation)

        val btnYes: Button = customDialog.findViewById(R.id.btnYes)
        val btnNo: Button = customDialog.findViewById(R.id.btnNo)

        btnYes.setOnClickListener {
            finish()
            customDialog.dismiss()
        }

        btnNo.setOnClickListener {
            customDialog.dismiss()
        }

        customDialog.show()
    }

}