package com.example.capytap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.isActive
import kotlinx.coroutines.yield

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App() }
    }
}

@Composable
fun App() {
    MaterialTheme {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            GameScreen()
        }
    }
}

@Composable
fun GameScreen() {
    var playing by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(30f) }
    var score by remember { mutableStateOf(0) }
    var highScore by remember { mutableStateOf(0) }

    var targetPos by remember { mutableStateOf(Offset(200f, 200f)) }
    var vx by remember { mutableStateOf(220f) }
    var vy by remember { mutableStateOf(180f) }
    val radiusDp: Dp = 28.dp
    val radiusPx = with(LocalDensity.current) { radiusDp.toPx() }

    LaunchedEffect(playing) {
        val frameTime = 1f / 120f
        while (playing && isActive) {
            timeLeft -= frameTime
            if (timeLeft <= 0f) {
                playing = false
                timeLeft = 0f
                if (score > highScore) highScore = score
                break
            }
            val newX = targetPos.x + vx * frameTime
            val newY = targetPos.y + vy * frameTime
            targetPos = targetPos.copy(x = newX, y = newY)
            yield()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0f172a)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Score: $score", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Text("Time: ${timeLeft.toInt()}s", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Text("Best: $highScore", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(12.dp)
                .background(Color(0xFF111827))
        ) {
            var canvasSize by remember { mutableStateOf(Offset(0f, 0f)) }

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(playing, targetPos) {
                        detectTapGestures { tap ->
                            if (!playing) return@detectTapGestures
                            val dx = tap.x - targetPos.x
                            val dy = tap.y - targetPos.y
                            val hit = dx * dx + dy * dy <= radiusPx * radiusPx
                            if (hit) {
                                score += 1
                                vx *= 1.06f
                                vy *= 1.06f
                                if (score % 3 == 0) vy = -vy
                            }
                        }
                    }
            ) {
                canvasSize = Offset(size.width, size.height)
                var x = targetPos.x
                var y = targetPos.y
                var changed = false
                if (x - radiusPx < 0) { x = radiusPx; vx = kotlin.math.abs(vx); changed = true }
                if (x + radiusPx > size.width) { x = size.width - radiusPx; vx = -kotlin.math.abs(vx); changed = true }
                if (y - radiusPx < 0) { y = radiusPx; vy = kotlin.math.abs(vy); changed = true }
                if (y + radiusPx > size.height) { y = size.height - radiusPx; vy = -kotlin.math.abs(vy); changed = true }
                if (changed) targetPos = Offset(x, y)
                drawCircle(color = Color(0xFF8B5E3C), radius = radiusPx, center = targetPos)
                drawCircle(color = Color.White, radius = radiusPx * 0.18f, center = targetPos + Offset(radiusPx * 0.35f, -radiusPx * 0.25f))
            }

            if (!playing) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .background(Color(0x99000000))
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        if (timeLeft <= 0f) "Time Up!" else "Capy Tap",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(12.dp))
                    if (timeLeft <= 0f) {
                        Text("Score: $score", color = Color.White, fontSize = 20.sp)
                        Text("Best: $highScore", color = Color.White, fontSize = 16.sp)
                        Spacer(Modifier.height(12.dp))
                    }
                    Button(onClick = {
                        playing = true
                        timeLeft = 30f
                        score = 0
                        vx = 220f; vy = 180f
                        targetPos = Offset(canvasSize.x / 2f, canvasSize.y / 2f)
                    }) {
                        Text(if (timeLeft <= 0f) "Play Again" else "Start")
                    }
                }
            }
        }
    }
}
