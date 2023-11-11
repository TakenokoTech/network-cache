package tech.takenoko.android.ncache

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tech.takenoko.android.ncache.databinding.FragmentFirstBinding
import tech.takenoko.android.ncache.domain.IApiRepository
import tech.takenoko.android.ncache.domain.ITestRepository
import tech.takenoko.android.ncache.entity.TestResponse
import tech.takenoko.android.ncache.repository.ApiRepository
import tech.takenoko.android.ncache.repository.MapRepository
import tech.takenoko.android.ncache.repository.NetworkCache
import tech.takenoko.android.ncache.repository.TestRepository

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        checkApi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkApi() = lifecycleScope.launch(Dispatchers.IO) {
        val apiRepository = ApiRepository(requireContext()) as IApiRepository
        val testRepository = TestRepository(requireContext()) as ITestRepository
        val mapRepository = MapRepository(requireContext())
        println(NetworkCache.clean(requireContext()))

        val map = mapOf(
            "key1" to "value1",
            "key2" to true,
            "key3" to 1111,
            "key4" to arrayOf("1", 2, true),
        )
        println(mapRepository.load("key1"))
        println(mapRepository.save(map))
        println(mapRepository.load("key1"))
        println(mapRepository.load("key2"))
        println(mapRepository.load("key3"))
        println(mapRepository.load("key4"))

        repeat(2) {
            apiRepository.fetch(1).also(::println).getOrNull()
            apiRepository.fetch(2).also(::println).getOrNull()
            apiRepository.fetch(3).also(::println).getOrNull()
            apiRepository.fetch(4).also(::println).getOrNull()
        }
        println(testRepository.load("test"))
        println(testRepository.save("test", TestResponse(1)))
        do {
            delay(1000)
            println(testRepository.load("test"))
        } while (testRepository.load("test").isSuccess)
    }
}
