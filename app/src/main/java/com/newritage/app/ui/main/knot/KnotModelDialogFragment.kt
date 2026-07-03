package com.newritage.app.ui.main.knot

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.DialogFragment
import com.newritage.app.R

/**
 * 매듭 보관함 캘린더에서 매듭이 기록된 날짜를 탭했을 때 해당 매듭의 GLB 3D 모델을 보여주는 다이얼로그.
 */
class KnotModelDialogFragment : DialogFragment() {

    companion object {
        private const val ARG_DATE = "arg_date"

        // TODO: 매듭별로 다른 모델을 쓰게 되면 날짜에 따라 경로를 분기한다.
        private const val KNOT_MODEL_ASSET_PATH = "models/knot.glb"

        fun newInstance(date: String): KnotModelDialogFragment {
            return KnotModelDialogFragment().apply {
                arguments = Bundle().apply { putString(ARG_DATE, date) }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = requireArguments().getString(ARG_DATE).orEmpty()
        val dialog = Dialog(requireContext())
        dialog.setContentView(
            ComposeView(requireContext()).apply {
                setContent {
                    KnotModelDialogContent(
                        date = date,
                        glbAssetPath = KNOT_MODEL_ASSET_PATH,
                        onClose = { dismiss() }
                    )
                }
            }
        )
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        return dialog
    }
}

@Composable
private fun KnotModelDialogContent(
    date: String,
    glbAssetPath: String,
    onClose: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = date,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium
                )
                TextButton(onClick = onClose) {
                    Text(text = stringResource(R.string.btn_close))
                }
            }
            KnotModelViewer(
                glbAssetPath = glbAssetPath,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}
