package com.app.myarticleapp.ui.bottom_sheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.myarticleapp.apiSource.responseEntity.Duration
import com.app.myarticleapp.databinding.FragmentMoreNewsSheetBinding
import com.app.myarticleapp.ui.adapters.DurationAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*


class MoreNewsSheet(private var result: (String) -> Unit) : BottomSheetDialogFragment(), DurationAdapter.OnItemClickListener {
    private var _binding: FragmentMoreNewsSheetBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DurationAdapter
    private var items = LinkedList<Duration>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { value ->
                val behaviour = BottomSheetBehavior.from(value)
                setupFullHeight(value)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = 400
        bottomSheet.layoutParams = layoutParams
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addItems()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       _binding = FragmentMoreNewsSheetBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun addItems(){
        items.add(Duration("1"))
        items.add(Duration("7"))
        items.add(Duration("30"))

        initAdapter(items)
    }

    private fun initAdapter(item: List<Duration>){
        binding.durationRecyclerView.layoutManager = LinearLayoutManager(binding.root.context)
        adapter = DurationAdapter(item, this)
        binding.durationRecyclerView.adapter = adapter
    }

    companion object {
         const val PERIOD = "period"
    }

    override fun onItemClick(position: Int, item: Duration) {
        result(item.period)
        dismiss()
    }
}