package com.example.myaudiobookplayer



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class ControlFragment  : Fragment() {

    private var book:Book? = null;
    private lateinit var tvBookTitle:TextView;
    private lateinit var tvStatus:TextView;
    private lateinit var stopPlaying:ImageButton;
    private lateinit var btnPlay:ImageButton;
    private lateinit var btnPause:ImageButton;
    private lateinit var seekBar:SeekBar;
    private val viewModel: AppViewModel by activityViewModels()

    //2
    companion object {

        fun newInstance(book:Book): ControlFragment {
            val fragmentDetails = ControlFragment();
            fragmentDetails.book = book
            return fragmentDetails;
        }
        fun newInstance(): ControlFragment {
            val fragmentDetails = ControlFragment();
            return fragmentDetails;
        }
    }
    //3
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.frag_control, container, false)
        findView(view)
        return view;
    }

    private fun findView(view: View){
        tvBookTitle = view.findViewById(R.id.tvBookTitle)
        stopPlaying = view.findViewById(R.id.stopPlaying)
        btnPlay = view.findViewById(R.id.btnPlay)
        btnPause = view.findViewById(R.id.btnPause)
        tvStatus = view.findViewById(R.id.tvStatus)
        seekBar = view.findViewById(R.id.seekBar)
        seekBar.max = 100;
        seekBar.min = 0;
        seekBar.progress = 0;
        seekBarListener()
        initData();
    }


    private fun initData(){
        clickListener()
        viewModel.bookToPlay.observe(viewLifecycleOwner){
            this.book = it
            showBookTitle()
        }

        viewModel.bookProgress.observe(viewLifecycleOwner){
            if(it != null) {
                if(it <= 100) {
                    tvStatus.text = "Now Playing "
                }
                seekBar.progress = it
            }
        }
    }

    private fun seekBarListener(){
        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            var progressChangedValue = 0
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                progressChangedValue = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if(book != null){
                    setSeek(progressChangedValue)
                }else{
                    seekBar.progress = 0
                }

            }
        })
    }

    private fun setSeek(progressChangedValue: Int){
        var seek = ((progressChangedValue)*(book!!.duration)) / 100
        viewModel.setSeekBar(seek)
    }


    private fun clickListener(){

        stopPlaying.setOnClickListener{
            if(book != null){
                tvStatus.visibility = View.GONE
                viewModel.stopPlayBook(book!!)
            }else{
                showMessage("No book selected")
            }
        }
        btnPlay.setOnClickListener{
            if(book != null){
                viewModel.startPlayBook(book!!)
            }else{
                showMessage("No book selected")
            }
        }
        btnPause.setOnClickListener{
            if(book != null) {
                tvStatus.visibility = View.GONE
                viewModel.pausePlayBook(book!!)
            }else{
                showMessage("No book selected")

            }

        }


    }

    private fun showBookTitle(){
        if(book != null) {
            tvBookTitle.text = book!!.title
        }
    }

    private fun showMessage(message: String){
        Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
    }


}