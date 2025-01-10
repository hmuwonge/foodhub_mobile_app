package com.metadent.foodhub_android.ui.features.auth.signUp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metadent.foodhub_android.R
import com.metadent.foodhub_android.ui.theme.Orange
import com.metadent.foodhub_android.ui.widgets.FoodHubTextField
import com.metadent.foodhub_android.ui.widgets.GroupSocialButtons

@Composable
fun SignUpScreen()
{
Box(modifier =Modifier.fillMaxSize() ){
    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }
    Image(painter = painterResource(id= R.drawable.ic_auth_bg),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds)

    Column(
        modifier = Modifier.fillMaxSize().
        padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(id=R.string.sign_up),
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier=Modifier.size(20.dp))
        FoodHubTextField(
            value =name,
            onValueChange = {name=it},
            label={
                Text(text= stringResource(id=R.string.full_name),color = Color.Gray,
                    textAlign =TextAlign.Center)
            },
            placeholder = {
                Text(text= stringResource(id=R.string.full_name_placeholder))
            },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier=Modifier.size(20.dp))
        FoodHubTextField(
            value = email,
            onValueChange = {email=it},
            label={
                Text(text= stringResource(id=R.string.email), color = Color.Gray)
            },
            placeholder = {
                Text(text= stringResource(id=R.string.email_placeholder))
            },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier=Modifier.size(20.dp))
        //pasword field
        FoodHubTextField(
            value = password,
            onValueChange = {password=it},
            label={
                Text(text= stringResource(id=R.string.password),color = Color.Gray)
            },
            placeholder = {
                Text(text= stringResource(id=R.string.password))
            },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            trailingIcon = {
                Image(
                    painter = painterResource(id=R.drawable.ic_eye),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        )
        Spacer(modifier=Modifier.size(20.dp))
        Button(
            onClick = {},
            modifier = Modifier.height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Orange)
            ) {
            Text(
                text = stringResource(
                    id=R.string.sign_up
                ),
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
        Spacer(modifier=Modifier.size(20.dp))
        Text(
            text = stringResource(id=R.string.already_have_account),
            modifier = Modifier.padding(8.dp)
                .clickable {  }
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier=Modifier.size(20.dp))
        GroupSocialButtons(
            color = Color.Black,
            onFacebookClick = {}
        ) { }
    }
}
}

@Preview(showBackground = true)
@Composable
fun PreviewSignUpScreen()
{
    SignUpScreen()
}