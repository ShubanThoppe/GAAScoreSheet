package com.example.gaascoresheet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import com.example.gaascoresheet.ui.theme.GAAScoreSheetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GAAScoreSheetTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) { innerPadding ->
                    ScoreSheet(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PopupBox(
    popupWidth: Float,
    popupHeight: Float,
    showPopup: Boolean,
    onClickOutside: () -> Unit,
    content: @Composable() () -> Unit
) {
    if (showPopup) {
        Popup(
            alignment = Alignment.Center,
            properties = PopupProperties(
                dismissOnClickOutside = true, // dismiss on click outside
                focusable = true  // ensure the popup can handle focus events
            ),
            onDismissRequest = { onClickOutside() }
        ) {
            Box(
                Modifier
                    .width(popupWidth.dp)
                    .height(popupHeight.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        modifier = modifier
    )
}

@Composable
fun ScoreScreen(modifier: Modifier = Modifier) {
    var teamAName by remember { mutableStateOf("Home") }
    var teamBName by remember { mutableStateOf("Away") }

    var teamAGoal by remember { mutableStateOf(0) }
    var teamAPoint by remember { mutableStateOf(0) }
    var teamBGoal by remember { mutableStateOf(0) }
    var teamBPoint by remember { mutableStateOf(0) }

    var showPopup by rememberSaveable { mutableStateOf(false) }  // State to control popup visibility

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Team A Section Box
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFE0E0E0))
                    .padding(16.dp)
            ) {
                TeamScoreSection(
                    teamName = teamAName,
                    onNameChange = { teamAName = it },
                    goals = teamAGoal,
                    point = teamAPoint,
                    onAddGoal = { teamAGoal++ },
                    onSubtractGoal = { if (teamAGoal > 0) teamAGoal-- },
                    onAddPoint = { teamAPoint++ },
                    onSubtractPoint = { if (teamAPoint > 0) teamAPoint-- },
                    totalScore = (teamAGoal * 3) + teamAPoint
                )
            }

            // Team B Section Box
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE0E0E0))
                    .padding(16.dp)
            ) {
                TeamScoreSection(
                    teamName = teamBName,
                    onNameChange = { teamBName = it },
                    goals = teamBGoal,
                    point = teamBPoint,
                    onAddGoal = { teamBGoal++ },
                    onSubtractGoal = { if (teamBGoal > 0) teamBGoal-- },
                    onAddPoint = { teamBPoint++ },
                    onSubtractPoint = { if (teamBPoint > 0) teamBPoint-- },
                    totalScore = (teamBGoal * 3) + teamBPoint
                )
            }
        }

        Button(
            onClick = {
                showPopup = true  // Show popup when button is clicked
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = "Reset",
                fontSize = 20.sp,
                color = Color.Black
            )
        }

        // Show popup confirmation dialog
        if (showPopup) {
            PopupBox(
                popupWidth = 300f,
                popupHeight = 250f,
                showPopup = showPopup,
                onClickOutside = { showPopup = false }  // Dismiss popup on click outside
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Are you sure you want to reset the scores?",
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = {
                                // Reset scores
                                teamAGoal = 0
                                teamAPoint = 0
                                teamBGoal = 0
                                teamBPoint = 0
                                showPopup = false
                            }
                        ) {
                            Text(text = "Yes", color = Color.Black)
                        }

                        Button(
                            onClick = {
                                showPopup = false
                            }
                        ) {
                            Text(text = "No", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun TeamScoreSection(
    teamName: String,
    onNameChange: (String) -> Unit,
    goals: Int,
    point: Int,
    onAddGoal: () -> Unit,
    onSubtractGoal: () -> Unit,
    onAddPoint: () -> Unit,
    onSubtractPoint: () -> Unit,
    totalScore: Int
) {
    var isTyping by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Team Name TextField
        BasicTextField(
            value = if (!isTyping && teamName == "Home" || teamName == "Away") "" else teamName,
            onValueChange = { newText ->
                val filteredText = newText.replace("\n", "")
                if (!isTyping) {
                    isTyping = true
                }
                onNameChange(filteredText)
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                        .padding(8.dp),
                ) {
                    if (!isTyping && (teamName == "Home" || teamName == "Away")) {
                        Text(
                            text = teamName,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                    }
                    innerTextField()
                }
            },
            singleLine = true,
        )

        // Goals Section
        Row(
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(text = "Goals:", fontSize = 20.sp, color = Color.Black)
            Button(onClick = onSubtractGoal) {
                Text(text = "-", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
            Text(text = "$goals", fontSize = 30.sp, color = Color.Black)
            Button(onClick = onAddGoal) {
                Text(text = "+", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }

        // Points Section
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(text = "Points:", fontSize = 20.sp, color = Color.Black)
            Button(onClick = onSubtractPoint) {
                Text(text = "-", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
            Text(text = "$point", fontSize = 30.sp, color = Color.Black)
            Button(onClick = onAddPoint) {
                Text(text = "+", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }

        // Total Score
        Text(
            text = "Total: $totalScore",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
    }
}

@Composable
fun ScoreSheet(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            TopAppBar()
        }
    ) { contentPadding ->
        ScoreScreen(
            modifier = Modifier.padding(contentPadding)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ScoreSheetPreview() {
    GAAScoreSheetTheme {
        ScoreSheet()
    }
}
