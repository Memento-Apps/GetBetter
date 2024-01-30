package com.velkonost.getbetter.core.compose.components.ad

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.my.target.ads.MyTargetView
import com.my.target.ads.MyTargetView.AdSize
import com.my.target.common.models.IAdLoadingError
import com.velkonost.getbetter.core.compose.components.Loader
import com.velkonost.getbetter.core.compose.components.PrimaryBox
import com.velkonost.getbetter.shared.resources.SharedR
import dev.icerock.moko.resources.compose.colorResource
import dev.icerock.moko.resources.compose.stringResource
import java.util.Locale


@Composable
fun AdView(
    modifier: Modifier = Modifier,
    slotId: Int
) {

    PrimaryBox(
        modifier = modifier
            .padding(PaddingValues(horizontal = 20.dp))
            .height(300.dp),
        padding = 0
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(bottom = 30.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier.weight(1f))
            Loader()
            Text(
                text = stringResource(resource = SharedR.strings.ad_title).replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.ROOT
                    ) else it.toString()
                },
                color = colorResource(resource = SharedR.colors.onboarding_background_gradient_start),
                style = MaterialTheme.typography.headlineSmall,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier.weight(1f))
        }

        Column {
            Spacer(modifier.weight(1f))
            Row {
                Spacer(modifier.weight(1f))

                AndroidView(
                    modifier = modifier
                        .clip(MaterialTheme.shapes.small),
                    factory = {
                        val view = MyTargetView(it)
                        view.setSlotId(slotId)
                        view.setAdSize(AdSize.ADSIZE_300x250)

                        view.listener = object : MyTargetView.MyTargetViewListener {
                            override fun onLoad(myTargetView: MyTargetView) {
                                // Данные успешно загружены, запускаем показ объявлений
//                                layout.addView(adView)
                            }

                            override fun onNoAd(p0: IAdLoadingError, myTargetView: MyTargetView) {
                                Log.d("ad", p0.message)
                            }

                            override fun onShow(myTargetView: MyTargetView) {

                            }

                            override fun onClick(myTargetView: MyTargetView) {

                            }
                        }

                        view.load()
                        view
                    }
                )
                Spacer(modifier.weight(1f))
            }
            Spacer(modifier.weight(1f))
        }
    }
}