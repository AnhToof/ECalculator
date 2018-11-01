package com.example.toof.ecalculator


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.widget.Button
import android.widget.TextView

class CalculatorFragment : Fragment() {
    private lateinit var mBtn0: Button
    private lateinit var mBtn1: Button
    private lateinit var mBtn2: Button
    private lateinit var mBtn3: Button
    private lateinit var mBtn4: Button
    private lateinit var mBtn5: Button
    private lateinit var mBtn6: Button
    private lateinit var mBtn7: Button
    private lateinit var mBtn8: Button
    private lateinit var mBtn9: Button
    private lateinit var mBtnMinus: Button
    private lateinit var mBtnPlus: Button
    private lateinit var mBtnDivide: Button
    private lateinit var mBtnMulti: Button
    private lateinit var mBtnDot: Button
    private lateinit var mBtnPercent: Button
    private lateinit var mBtnEqual: Button
    private lateinit var mBtnAC: Button
    private lateinit var mBtnSign: Button
    private lateinit var mTextViewConsole: TextView
    private lateinit var mPreferences: SharedPreferences
    private lateinit var mEditor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mPreferences = activity!!.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        mEditor = mPreferences.edit()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> {
                mEditor.putString("RESULT", mTextViewConsole.text.toString())
                mEditor.apply()
                activity!!.toast("Saved")
            }
            R.id.menu_clear -> {
                mEditor.clear()
                mEditor.apply()
                activity!!.toast("Cleared")
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calculator, container, false)
        init(view)
        if (mPreferences.getString("RESULT", null) != null ){
            mTextViewConsole.text = mPreferences.getString("RESULT", null)
        }
        return view
    }

    private fun init(view: View) {
        mBtn0 = view.findViewById(R.id.btn_0)
        mBtn1 = view.findViewById(R.id.btn_1)
        mBtn2 = view.findViewById(R.id.btn_2)
        mBtn3 = view.findViewById(R.id.btn_3)
        mBtn4 = view.findViewById(R.id.btn_4)
        mBtn5 = view.findViewById(R.id.btn_5)
        mBtn6 = view.findViewById(R.id.btn_6)
        mBtn7 = view.findViewById(R.id.btn_7)
        mBtn8 = view.findViewById(R.id.btn_8)
        mBtn9 = view.findViewById(R.id.btn_9)
        mBtnDivide = view.findViewById(R.id.btn_divide)
        mBtnMinus = view.findViewById(R.id.btn_minus)
        mBtnMulti = view.findViewById(R.id.btn_multi)
        mBtnPlus = view.findViewById(R.id.btn_plus)
        mBtnPercent = view.findViewById(R.id.btn_percent)
        mBtnDot = view.findViewById(R.id.btn_dot)
        mBtnAC = view.findViewById(R.id.btn_ac)
        mBtnEqual = view.findViewById(R.id.btn_equal)
        mBtnSign = view.findViewById(R.id.btn_change)
        mTextViewConsole = view.findViewById(R.id.text_console)
        mTextViewConsole.movementMethod = ScrollingMovementMethod()
        mTextViewConsole.isSelected = true

        mBtn0.setOnClickListener {
            if (mTextViewConsole.text.toString() != "0") {
                mTextViewConsole.append("0")
            }
        }
        mBtn1.setOnClickListener {
            handleNumber("1")

        }
        mBtn2.setOnClickListener {
            handleNumber("2")

        }
        mBtn3.setOnClickListener {
            handleNumber("3")

        }
        mBtn4.setOnClickListener {
            handleNumber("4")
        }
        mBtn5.setOnClickListener {
            handleNumber("5")
        }
        mBtn6.setOnClickListener {
            handleNumber("6")
        }
        mBtn7.setOnClickListener {
            handleNumber("7")
        }
        mBtn8.setOnClickListener {
            handleNumber("8")
        }
        mBtn9.setOnClickListener {
            handleNumber("9")
        }
        mBtnDivide.setOnClickListener {
            handleOperator("/")
        }

        mBtnMulti.setOnClickListener {
            handleOperator("x")
        }
        mBtnMinus.setOnClickListener {
            handleOperator("-")
        }
        mBtnPlus.setOnClickListener {
            handleOperator("x")
        }
        mBtnPercent.setOnClickListener {
            val expression = mTextViewConsole.text.toString()
            if (!expression.isEmpty()) {
                for (i in 0 until expression.length) {
                    if (!isOperation(expression[i].toString())) {
                        mTextViewConsole.text = (expression.toDouble() / 100).toString()
                    }
                }
            }
        }
        mBtnSign.setOnClickListener {
            val expression = mTextViewConsole.text.toString()
            mTextViewConsole.text = (expression.toDouble() * -1).toString()
        }
        mBtnDot.setOnClickListener {
            mTextViewConsole.append(".")
        }
        mBtnAC.setOnClickListener {
            mTextViewConsole.text = "0"
        }
        mBtnEqual.setOnClickListener {
            val expression = mTextViewConsole.text.toString()
            for (i in 0 until expression.length) {
                if (isOperation(expression[i].toString())) {
                    val op = expression[i].toString()
                    val operatorIndex = expression.indexOf(op)
                    calculate(
                        expression.substring(0, operatorIndex).toDouble(),
                        op,
                        expression.substring(operatorIndex + 1, expression.length).toDouble()
                    )
                }
            }
        }

    }

    private fun handleOperator(operator: String) {
        val expression = mTextViewConsole.text.toString()
        for (i in 0 until expression.length) {
            if (isOperation(expression[i].toString())) {
                val op = expression[i].toString()
                val operatorIndex = expression.indexOf(op)
                calculate(
                    expression.substring(0, operatorIndex).toDouble(),
                    op,
                    expression.substring(operatorIndex + 1, expression.length).toDouble()
                )
            } else {
                mTextViewConsole.append(operator)
                break
            }
        }
    }

    private fun handleNumber(value: String) {
        if (mTextViewConsole.text.toString() == "0") {
            mTextViewConsole.text = value
        } else {
            mTextViewConsole.append(value)
        }
    }

    private fun isOperation(value: String) = when (value) {
        "x" -> true
        "/" -> true
        "-" -> true
        "+" -> true
        else -> false
    }

    private fun calculate(value1: Double, operator: String, value2: Double) {
        when (operator) {
            "x" -> mTextViewConsole.text = (value1 * value2).toString()
            "/" -> mTextViewConsole.text = (value1 / value2).toString()
            "+" -> mTextViewConsole.text = (value1 + value2).toString()
            "-" -> mTextViewConsole.text = (value1 - value2).toString()
        }
    }

}


