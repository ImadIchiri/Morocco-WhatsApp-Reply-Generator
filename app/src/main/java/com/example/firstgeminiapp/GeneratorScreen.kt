package com.example.firstgeminiapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firstgeminiapp.ui.theme.BananaDarkGreen
import com.example.firstgeminiapp.ui.theme.BananaGreen
import com.example.firstgeminiapp.ui.theme.BananaYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneratorScreen(
    viewModel: GeneratorViewModel = viewModel()
) {
    var receivedMessage by rememberSaveable { mutableStateOf("") }
    var goal by rememberSaveable { mutableStateOf("") }
    var tone by rememberSaveable { mutableStateOf("") }
    var context by rememberSaveable { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.app_name),
                        fontWeight = FontWeight.Bold,
                        color = BananaDarkGreen
                    ) 
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BananaYellow
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Input Section
            OutlinedTextField(
                value = receivedMessage,
                onValueChange = { receivedMessage = it },
                label = { Text(stringResource(R.string.label_received_message)) },
                placeholder = { Text(stringResource(R.string.prompt_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = goal,
                onValueChange = { goal = it },
                label = { Text(stringResource(R.string.label_goal)) },
                placeholder = { Text(stringResource(R.string.goal_placeholder)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = tone,
                onValueChange = { tone = it },
                label = { Text(stringResource(R.string.label_tone)) },
                placeholder = { Text(stringResource(R.string.tone_placeholder)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = context,
                onValueChange = { context = it },
                label = { Text(stringResource(R.string.label_context)) },
                placeholder = { Text(stringResource(R.string.context_placeholder)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (receivedMessage.isNotBlank() && goal.isNotBlank()) {
                        viewModel.generateReplies(receivedMessage, goal, tone, context)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = BananaGreen),
                enabled = uiState !is GeneratorUiState.Loading
            ) {
                if (uiState is GeneratorUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(stringResource(R.string.action_generate), color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Results Section
            when (val state = uiState) {
                is GeneratorUiState.Success -> {
                    Text(
                        stringResource(R.string.title_results),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    ReplyCard(stringResource(R.string.label_short), state.short) {
                        clipboardManager.setText(AnnotatedString(state.short))
                    }
                    ReplyCard(stringResource(R.string.label_professional), state.professional) {
                        clipboardManager.setText(AnnotatedString(state.professional))
                    }
                    ReplyCard(stringResource(R.string.label_friendly), state.friendly) {
                        clipboardManager.setText(AnnotatedString(state.friendly))
                    }
                    
                    Button(
                        onClick = { viewModel.reset() },
                        modifier = Modifier.padding(top = 16.dp),
                        variant = ButtonDefaults.textButtonColors()
                    ) {
                        Text("Clear All")
                    }
                }
                is GeneratorUiState.Error -> {
                    Text(
                        state.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {}
            }
        }
    }
}

@Composable
fun ReplyCard(label: String, content: String, onCopy: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    color = BananaGreen,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onCopy) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = stringResource(R.string.action_copy),
                        tint = BananaGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

// Extension to allow custom variant (simulated)
@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    variant: ButtonColors? = null,
    content: @Composable RowScope.() -> Unit
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = variant ?: colors,
        content = content
    )
}