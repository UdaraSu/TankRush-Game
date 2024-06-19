package com.example.tankrush


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

class GameView(var c :Context,var gameTask :GameTask): View(c)
{
    private var myPaint: Paint? = null
    private var speed = 1
    private var time = 0
    private var score = 0
    private var myTankPosition = 0
    private val otherTanks = ArrayList<HashMap<String,Any>>()

    var viewWidth = 0
    var viewHeight = 0
    init {
        myPaint = Paint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        viewWidth = this.measuredWidth
        viewHeight = this.measuredHeight

        if(time % 700 < 10 +speed){
            val map = HashMap<String,Any>()
            map["lane"] = (0..2).random()
            map["startTime"] = time
            otherTanks.add(map)
        }
        time = time + 10 + speed
        val tankWidth = viewWidth / 5
        val tankHeight = tankWidth + 10
        myPaint!!.style = Paint.Style.FILL
        val d = resources.getDrawable(R.drawable.tank, null)

        d.setBounds(
            myTankPosition * viewWidth / 3 + viewWidth / 15 + 25,
            viewHeight-2 - tankHeight,
            myTankPosition * viewWidth / 3 + viewWidth / 15 + tankWidth - 25 ,
            viewHeight - 2
        )
        d.draw(canvas!!)
        myPaint!!.color = Color.GREEN
        var highScore = 0

        for (i in otherTanks.indices){
            try {
                val tankX = otherTanks[i]["lane"] as Int * viewWidth / 3 + viewWidth / 15
                var tankY = time - otherTanks[i]["startTime"] as Int
                val d2 = resources.getDrawable(R.drawable.enemy, null)
                d2.setBounds(
                    tankX + 25 , tankY - tankHeight, tankX + tankWidth - 25 , tankY
                )
                d2.draw(canvas)
                if (otherTanks[i]["lane"] as Int == myTankPosition) {
                    if (tankY > viewHeight - 2 - tankHeight
                        && tankY < viewHeight - 2){

                        gameTask.closeGame(score)
                    }

                }
                if (tankY > viewHeight + tankHeight)
                {
                    otherTanks.removeAt(i)
                    score++
                    speed = 1 + Math.abs(score / 8)
                    if (score > highScore) {
                        highScore = score
                    }
                }

            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
        myPaint!!.color =  Color.WHITE
        myPaint!!.textSize = 40f
        canvas.drawText("Score : $score", 80f, 80f, myPaint!!)
        canvas.drawText("Speed : $speed", 380f, 80f, myPaint!!)
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action){
            MotionEvent.ACTION_DOWN ->{
                val x1 =  event.x
                if(x1<viewWidth/2) {
                    if (myTankPosition > 0) {
                        myTankPosition--
                    }
                }
                if(x1 > viewWidth/2){
                    if(myTankPosition<2){
                        myTankPosition++
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP-> {}
        }
        return true
    }
}