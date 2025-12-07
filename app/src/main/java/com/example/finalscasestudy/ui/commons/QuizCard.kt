package com.example.finalscasestudy.ui.commons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizCard(
    quizName: String,
    difficulty: String,
    itemCount: Int = 10,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .height(150.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Column: Quiz Name & Difficulty
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.7f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = quizName,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.SemiBold
                )

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 30.dp, top = 15.dp, bottom = 15.dp),
                    thickness = 3.dp
                )

                Row {
                    Text(
                        modifier = Modifier.padding(start = 15.dp),
                        text = "Difficulty Rating:",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        modifier = Modifier.padding(start = 5.dp),
                        text = difficulty,
                        fontSize = 15.sp
                    )
                }
            }

            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 15.dp),
                thickness = 3.dp
            )

            // Right Column: Item Count
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.padding(start = 15.dp, bottom = 3.dp),
                    text = itemCount.toString(),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = "Items",
                    fontSize = 20.sp
                )
            }
        }
    }
}
