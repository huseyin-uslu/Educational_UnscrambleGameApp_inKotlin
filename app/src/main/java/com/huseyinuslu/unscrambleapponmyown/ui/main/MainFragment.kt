package com.huseyinuslu.unscrambleapponmyown.ui.main

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.huseyinuslu.unscrambleapponmyown.R
import com.huseyinuslu.unscrambleapponmyown.databinding.MainFragmentBinding
import com.huseyinuslu.unscrambleapponmyown.ui.main.data.AllOfWords

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel : MainViewModel by viewModels()

    private  var _binding : MainFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

      _binding = DataBindingUtil.inflate(inflater,R.layout.main_fragment,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeVariables()
    }

    private fun observeVariables(){

        binding.mainViewModel = viewModel
        binding.maxNoOfWords = AllOfWords.MAX_OF_NO_WORDS

        viewModel.getNewUnscrambledWord()

        viewModel.score.observe(viewLifecycleOwner){changedInt ->
            changedInt?.let {
                binding.score = it
            }
        }

        viewModel.countOfWords.observe(viewLifecycleOwner){changedCount ->
            changedCount?.let {
                binding.wordsCount = it
            }
        }

        binding.apply {
            submitButton.setOnClickListener{submitWord()}
            skipButton.setOnClickListener{skipWord()}
        }
    }

    private fun submitWord(){
        val answer = binding.editTextFieldForInput.text.toString()
    if(viewModel.isAnswerCorrect(answer)){
        setErrorAccordingToWhetherAnswerIsCorrectOrNot(true)
        if(viewModel.areThereAnyWordsLeft()){
            viewModel.getNewUnscrambledWord()
        }else{
            onWordsFinished()
        }
    }else{
        setErrorAccordingToWhetherAnswerIsCorrectOrNot(false)
    }

    }

    private fun skipWord(){
        if(viewModel.areThereAnyWordsLeft()){
            viewModel.getNewUnscrambledWord()
        }else{
            onWordsFinished()
        }
    }

    //TODO: this is where there is an error running the simulator..
    private fun setErrorAccordingToWhetherAnswerIsCorrectOrNot(error : Boolean){
        if(error){
            binding.editTextFieldForInput.error = resources.getString(R.string.set_error_false)
            binding.textInputLayout.error = resources.getString(R.string.set_error_false)
            binding.textInputLayout.isErrorEnabled = true
        }else{
            binding.editTextFieldForInput.error = null
            binding.textInputLayout.isErrorEnabled = false
        }
    }

    private fun onWordsFinished(){
        alertDialog(resources.getString(R.string.game_over),
            resources.getString(R.string.you_scored_message,viewModel.score.value!!)
        ,resources.getString(R.string.restart_game),
        resources.getString(R.string.exit_game))
    }

    private fun alertDialog(title: String, message: String,positiveOption : String,negativeOption : String) {
       MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveOption){_,_, ->
                restartGame()
            }.setNegativeButton(negativeOption){_,_, ->
               exitGame()
           }.show()
    }

    private fun restartGame(){
        viewModel.restartGame()
    }

    private fun exitGame(){
        activity?.finish()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}