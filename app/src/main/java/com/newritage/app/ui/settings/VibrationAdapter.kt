package com.newritage.app.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newritage.app.R
import com.newritage.app.ble.VibrationCategory
import com.newritage.app.ble.VibrationPattern
import com.newritage.app.databinding.ItemVibrationBinding
import com.newritage.app.databinding.ItemVibrationHeaderBinding

/** 진동 선택 목록의 한 행. 카테고리 섹션 헤더와 개별 패턴 항목을 함께 나열하기 위한 래퍼. */
sealed class VibrationListItem {
    data class Header(val category: VibrationCategory) : VibrationListItem()
    data class Row(val pattern: VibrationPattern) : VibrationListItem()
}

class VibrationAdapter(
    private val items: List<VibrationListItem>,
    private val onPlayClick: (VibrationPattern) -> Unit,
    private val onSelectClick: (VibrationPattern) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_ROW = 1
    }

    var enabled: Boolean = true
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var selectedId: String? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var playingId: String? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class HeaderViewHolder(val binding: ItemVibrationHeaderBinding) : RecyclerView.ViewHolder(binding.root)
    class RowViewHolder(val binding: ItemVibrationBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int = when (items[position]) {
        is VibrationListItem.Header -> VIEW_TYPE_HEADER
        is VibrationListItem.Row -> VIEW_TYPE_ROW
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_HEADER) {
            HeaderViewHolder(ItemVibrationHeaderBinding.inflate(inflater, parent, false))
        } else {
            RowViewHolder(ItemVibrationBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is VibrationListItem.Header -> (holder as HeaderViewHolder).binding.tvHeader.text = item.category.label
            is VibrationListItem.Row -> bindRow(holder as RowViewHolder, item.pattern)
        }
    }

    private fun bindRow(holder: RowViewHolder, pattern: VibrationPattern) {
        val isSelected = pattern.id == selectedId
        val isPlaying = pattern.id == playingId

        holder.binding.tvName.text = pattern.name
        holder.binding.ivCheck.setImageResource(
            if (isSelected) R.drawable.ic_check_circle_filled else R.drawable.ic_check_circle_outline
        )
        holder.binding.btnPlay.setImageResource(
            if (isPlaying) R.drawable.ic_pause_circle else R.drawable.ic_play_circle
        )

        holder.binding.root.alpha = if (enabled) 1f else 0.4f
        holder.binding.btnPlay.isEnabled = enabled
        holder.binding.root.isEnabled = enabled

        holder.binding.btnPlay.setOnClickListener { if (enabled) onPlayClick(pattern) }
        holder.binding.ivCheck.setOnClickListener { if (enabled) onSelectClick(pattern) }
        holder.binding.root.setOnClickListener { if (enabled) onSelectClick(pattern) }
    }

    override fun getItemCount(): Int = items.size
}
