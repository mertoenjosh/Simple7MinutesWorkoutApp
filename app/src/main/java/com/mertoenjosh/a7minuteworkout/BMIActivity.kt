package com.mertoenjosh.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputLayout
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {
    private lateinit var toolbar_bmi_activity: Toolbar
    private lateinit var rgUnits: RadioGroup
    private lateinit var etMetricUnitWeight: EditText
    private lateinit var etMetricUnitHeight: EditText
    private lateinit var etUsUnitWeight: EditText
    private lateinit var etUsUnitHeightFeet: EditText
    private lateinit var etUsUnitHeightInch: EditText
    private lateinit var btnCalculateUnits: Button
    private lateinit var tvYourBmi: TextView
    private lateinit var tvBMIValue: TextView
    private lateinit var tvBMIType: TextView
    private lateinit var tvBMIDescription: TextView
    private lateinit var llDisplayBMIResult: LinearLayout

    private lateinit var tilMetricUnitWeight: TextInputLayout
    private lateinit var tilMetricUnitHeight: TextInputLayout
    private lateinit var tilUsUnitWeight: TextInputLayout
    private lateinit var llUsUnitsHeight: LinearLayout

    val METRIC_UNITS_VIEW = "METRIC_UNITS_VIEW"
    val US_UNITS_VIEW = "US_UNITS_VIEW"

    var currentVisibleView = METRIC_UNITS_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmiactivity)

        toolbar_bmi_activity = findViewById(R.id.toolbar_bmi_activity)
        setSupportActionBar(toolbar_bmi_activity)
        val actionBar = supportActionBar

        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "CALCULATE BMI"

        toolbar_bmi_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        // Initialize UI elements
        rgUnits = findViewById(R.id.rgUnits)
        etMetricUnitWeight = findViewById(R.id.etMetricUnitWeight)
        etMetricUnitHeight = findViewById(R.id.etMetricUnitHeight)
        etUsUnitWeight = findViewById(R.id.etUsUnitWeight)
        etUsUnitHeightFeet = findViewById(R.id.etUsUnitHeightFeet)
        etUsUnitHeightInch = findViewById(R.id.etUsUnitHeightInch)
        etMetricUnitHeight = findViewById(R.id.etMetricUnitHeight)
        btnCalculateUnits = findViewById(R.id.btnCalculateUnits)
        tvYourBmi = findViewById(R.id.tvYourBmi)
        tvBMIValue = findViewById(R.id.tvBMIValue)
        tvBMIType = findViewById(R.id.tvBMIType)
        tvBMIDescription = findViewById(R.id.tvBMIDescription)
        tilMetricUnitWeight = findViewById(R.id.tilMetricUnitWeight)
        tilMetricUnitHeight = findViewById(R.id.tilMetricUnitHeight)
        tilUsUnitWeight = findViewById(R.id.tilUsUnitWeight)
        llUsUnitsHeight = findViewById(R.id.llUsUnitsHeight)
        llDisplayBMIResult = findViewById(R.id.llDisplayBMIResult)


        makeVisibleMetricUnitsView()
        rgUnits.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbMetricUnits) {
                makeVisibleMetricUnitsView()
            } else {
                makeVisibleUsUnitsView()
            }
        }


        btnCalculateUnits.setOnClickListener {
            if (currentVisibleView == METRIC_UNITS_VIEW) {
                if (validateMetricUnits()) {
                    val heightValue: Float = etMetricUnitHeight.text.toString().toFloat() / 100
                    val weightValue: Float = etMetricUnitWeight.text.toString().toFloat()

                    // BMI = W/(h * h)
                    val bmi = weightValue / (heightValue * heightValue)
                    displayBMIResult(bmi)
                } else {
                    Toast.makeText(this@BMIActivity, "Please enter valid values", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (validateUsUnits()) {
                    val usUnitHeightValueFeet: Int = etUsUnitHeightFeet.text.toString().toInt()
                    val usUnitHeightValueInch: Int = etUsUnitHeightInch.text.toString().toInt()
                    val usUnitWeightValue: Float = etUsUnitWeight.text.toString().toFloat()

                    val heightValue = usUnitHeightValueInch.toFloat() + (usUnitHeightValueFeet.toFloat() * 12)

                    val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))
                    displayBMIResult(bmi)

                } else {
                    Toast.makeText(this, "Please enter valid values", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    private fun clearInputs() {
        etMetricUnitWeight.text?.clear()
        etMetricUnitHeight.text?.clear()
        etUsUnitWeight.text.clear()
        etUsUnitHeightFeet.text.clear()
        etUsUnitHeightInch.text.clear()
    }

    private fun makeVisibleMetricUnitsView() {
        currentVisibleView = METRIC_UNITS_VIEW

        tilUsUnitWeight.visibility = View.GONE
        llUsUnitsHeight.visibility = View.GONE

//        etMetricUnitWeight.text?.clear()
//        etMetricUnitHeight.text?.clear()
        clearInputs()

        tilMetricUnitWeight.visibility = View.VISIBLE
        tilMetricUnitHeight.visibility = View.VISIBLE

        llDisplayBMIResult.visibility = View.INVISIBLE

    }

    private fun makeVisibleUsUnitsView() {
        currentVisibleView = US_UNITS_VIEW

        tilMetricUnitWeight.visibility = View.GONE
        tilMetricUnitHeight.visibility = View.GONE

//        etUsUnitWeight.text.clear()
//        etUsUnitHeightFeet.text.clear()
//        etUsUnitHeightInch.text.clear()
        clearInputs()

        tilUsUnitWeight.visibility = View.VISIBLE
        llUsUnitsHeight.visibility = View.VISIBLE

        llDisplayBMIResult.visibility = View.INVISIBLE
    }

    private fun displayBMIResult(bmi: Float) {
        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5) > 0 && bmi.compareTo(25f) <= 0) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Workout now!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0) {
            bmiLabel = "Obese class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take better care of yourself! Workout now!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0) {
            bmiLabel = "Obese class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese class ||| (Very severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        llDisplayBMIResult.visibility = View.VISIBLE

//        tvYourBmi.visibility = View.VISIBLE
//        tvBMIValue.visibility = View.VISIBLE
//        tvBMIType.visibility = View.VISIBLE
//        tvBMIDescription.visibility = View.VISIBLE

        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        tvBMIValue.text = bmiValue
        tvBMIType.text = bmiLabel
        tvBMIDescription.text = bmiDescription

    }

    private fun validateMetricUnits(): Boolean {
        var isValid = true

        if (etMetricUnitHeight.text.toString().isEmpty())
            isValid = false

        if (etMetricUnitWeight.text.toString().isEmpty())
            isValid = false

        return isValid
    }

    private fun validateUsUnits(): Boolean = !(etUsUnitHeightFeet.text.toString().isEmpty() ||
            etUsUnitHeightInch.text.toString().isEmpty() ||
            etUsUnitWeight.text.toString().isEmpty())

}