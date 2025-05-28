package com.example.hciAssignment.ui.screens.chatbot.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hciAssignment.domain.model.Message
import com.example.hciAssignment.domain.model.Performance
import com.example.hciAssignment.domain.model.performanceToString
import com.example.hciAssignment.ui.screens.chatbot.utils.ContactDetails
import com.example.hciAssignment.ui.theme.customColors

@Composable
private fun getMessageColors(message: Message): Pair<androidx.compose.ui.graphics.Color, androidx.compose.ui.graphics.Color> {
    val backgroundColor =
        if (message.isFromUser) customColors.userMessageBackground else customColors.botMessageBackground
    val textColor =
        if (message.isFromUser) customColors.userMessageText else customColors.botMessageText
    return backgroundColor to textColor
}

@Composable
private fun BaseMessageBubble(
    message: Message,
    content: @Composable ColumnScope.() -> Unit
) {
    val (backgroundColor, _) = getMessageColors(message)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = if (message.isFromUser) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .background(backgroundColor, RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .widthIn(max = 280.dp)
        ) {
            Column(content = content)
        }
    }
}

@Composable
fun MessageBubble(message: Message) {
    val (_, textColor) = getMessageColors(message)

    BaseMessageBubble(message) {
        if (message.isTypingIndicator) {
            TypingDots()
        } else {
            Text(text = message.message, color = textColor)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MessageBubbleWithActions(
    message: Message,
    performances: List<Performance>,
    onPerformanceSelected: (Performance) -> Unit,
    isEnabled: Boolean
) {
    val (_, textColor) = getMessageColors(message)

    BaseMessageBubble(message) {
        if (message.isTypingIndicator) {
            TypingDots()
        } else {
            Text(text = message.message, color = textColor)
        }

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            performances.forEach { performance ->
                ActionText(
                    label = performance.performanceToString(),
                    onClick = { onPerformanceSelected(performance) },
                    isEnabled = isEnabled
                )
            }
        }
    }
}

@Composable
fun MessageBubbleWithContactAction(
    message: Message,
    onContactClick: () -> Unit,
    isEnabled: Boolean
) {
    val (_, textColor) = getMessageColors(message)

    BaseMessageBubble(message) {
        if (message.isTypingIndicator) {
            TypingDots()
        } else {
            Text(text = message.message, color = textColor)
        }

        Spacer(modifier = Modifier.height(8.dp))

        ActionText(
            label = "Contact",
            onClick = onContactClick,
            isEnabled = isEnabled
        )
    }
}

@Composable
fun MessageBubbleWithContactDetails(
    message: Message,
    contactDetails: ContactDetails,
    isEnabled: Boolean
) {
    val (_, textColor) = getMessageColors(message)
    val context = LocalContext.current

    fun handleLinkClick(phone: String? = null, email: String? = null, website: String? = null) {
        val intent = when {
            phone != null -> Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
            email != null -> Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email"))
            website != null -> Intent(Intent.ACTION_VIEW, Uri.parse(website))
            else -> null
        }
        intent?.let { context.startActivity(it) }
    }

    BaseMessageBubble(message) {
        if (message.isTypingIndicator) {
            TypingDots()
        } else {
            Text(text = message.message, color = textColor)
        }

        Spacer(modifier = Modifier.height(4.dp))

        contactDetails.phone.let { phone ->
            ActionText(
                label = "Call $phone",
                onClick = { handleLinkClick(phone = phone) },
                isEnabled = isEnabled
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        contactDetails.email.let { email ->
            ActionText(
                label = "Email $email",
                onClick = { handleLinkClick(email = email) },
                isEnabled = isEnabled
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        contactDetails.website.let { website ->
            ActionText(
                label = "Website $website",
                onClick = { handleLinkClick(website = "https://$website") },
                isEnabled = isEnabled
            )
        }
    }
}

@Composable
private fun ActionText(
    label: String,
    onClick: () -> Unit,
    isEnabled: Boolean
) {
    val textColor = MaterialTheme.colorScheme.onPrimary
    val backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)

    Text(
        text = label,
        fontSize = 12.sp,
        color = textColor,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(enabled = isEnabled, onClick = onClick)
            .background(backgroundColor)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    )
}
