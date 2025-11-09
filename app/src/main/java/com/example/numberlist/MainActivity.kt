package com.example.numberlist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Khai báo các thành phần giao diện
    private lateinit var editTextNumber: EditText        // Ô nhập số nguyên
    private lateinit var radioGroup: RadioGroup          // Nhóm RadioButton (không dùng cho logic)
    private lateinit var radioEven: RadioButton          // RadioButton: Số chẵn
    private lateinit var radioOdd: RadioButton           // RadioButton: Số lẻ
    private lateinit var radioSquare: RadioButton        // RadioButton: Số chính phương
    private lateinit var radioPrime: RadioButton         // RadioButton: Số nguyên tố
    private lateinit var radioPerfectNumber: RadioButton // RadioButton: Số hoàn hảo
    private lateinit var radioFibonacci: RadioButton     // RadioButton: Số Fibonacci
    private lateinit var listView: ListView              // Danh sách hiển thị kết quả
    private lateinit var textViewEmpty: TextView         // TextView hiển thị thông báo rỗng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ẩn ActionBar
        supportActionBar?.hide()

        // Khởi tạo các view
        initViews()

        // Thiết lập các listener (lắng nghe sự kiện)
        setupListeners()
    }

    /**
     * Khởi tạo các view từ layout XML
     */
    private fun initViews() {
        editTextNumber = findViewById(R.id.editTextNumber)
        radioGroup = findViewById(R.id.radioGroup)
        radioEven = findViewById(R.id.radioEven)
        radioOdd = findViewById(R.id.radioOdd)
        radioSquare = findViewById(R.id.radioSquare)
        radioPrime = findViewById(R.id.radioPrime)
        radioPerfectNumber = findViewById(R.id.radioPerfectNumber)
        radioFibonacci = findViewById(R.id.radioFibonacci)
        listView = findViewById(R.id.listView)
        textViewEmpty = findViewById(R.id.textViewEmpty)

        // Mặc định chọn số lẻ
        radioOdd.isChecked = true
    }

    /**
     * Thiết lập các listener để lắng nghe sự kiện
     */
    private fun setupListeners() {
        // Lắng nghe thay đổi nội dung trong EditText
        editTextNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            // Sau khi text thay đổi → cập nhật danh sách
            override fun afterTextChanged(s: Editable?) {
                updateList()
            }
        })

        // Tạo danh sách tất cả RadioButton
        val radioButtons = listOf(
            radioOdd,
            radioEven,
            radioSquare,
            radioPrime,
            radioPerfectNumber,
            radioFibonacci
        )

        // Xử lý click thủ công để đảm bảo chỉ 1 RadioButton được chọn
        // (Vì RadioGroup không hoạt động tốt với layout 2 cột)
        radioButtons.forEach { radioButton ->
            radioButton.setOnClickListener {
                // Bước 1: Bỏ chọn TẤT CẢ RadioButton
                radioButtons.forEach { it.isChecked = false }

                // Bước 2: Chọn lại RadioButton vừa click
                radioButton.isChecked = true

                // Bước 3: Cập nhật danh sách số
                updateList()
            }
        }
    }

    /**
     * Cập nhật danh sách số dựa trên input và loại số được chọn
     */
    private fun updateList() {
        val inputStr = editTextNumber.text.toString()

        // Kiểm tra: Nếu EditText rỗng
        if (inputStr.isEmpty()) {
            showEmptyMessage("Vui lòng nhập số nguyên")
            return
        }

        try {
            // Chuyển chuỗi sang số nguyên
            val number = inputStr.toInt()

            // Kiểm tra: Số phải dương
            if (number <= 0) {
                showEmptyMessage("Vui lòng nhập số nguyên dương")
                return
            }

            // Xác định loại số được chọn và lấy danh sách tương ứng
            val numbers = when {
                radioOdd.isChecked -> getOddNumbers(number)
                radioEven.isChecked -> getEvenNumbers(number)
                radioSquare.isChecked -> getSquareNumbers(number)
                radioPrime.isChecked -> getPrimeNumbers(number)
                radioPerfectNumber.isChecked -> getPerfectNumbers(number)
                radioFibonacci.isChecked -> getFibonacciNumbers(number)
                else -> emptyList()
            }

            // Hiển thị kết quả
            if (numbers.isEmpty()) {
                showEmptyMessage("Không có số nào thỏa mãn")
            } else {
                showList(numbers)
            }

        } catch (e: NumberFormatException) {
            // Xử lý lỗi nếu nhập không phải số
            showEmptyMessage("Số nhập vào không hợp lệ")
        }
    }

    /**
     * Hiển thị danh sách số trong ListView
     */
    private fun showList(numbers: List<Int>) {
        textViewEmpty.visibility = View.GONE  // Ẩn thông báo rỗng
        listView.visibility = View.VISIBLE    // Hiện ListView

        // Tạo adapter và gán cho ListView
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            numbers
        )
        listView.adapter = adapter
    }

    /**
     * Hiển thị thông báo khi danh sách rỗng
     */
    private fun showEmptyMessage(message: String) {
        listView.visibility = View.GONE         // Ẩn ListView
        textViewEmpty.visibility = View.VISIBLE // Hiện thông báo
        textViewEmpty.text = message
    }

    // ==================== CÁC HÀM TÍNH TOÁN SỐ ====================

    /**
     * Lấy danh sách số chẵn nhỏ hơn max
     * Ví dụ: max=10 → [2, 4, 6, 8]
     */
    private fun getEvenNumbers(max: Int): List<Int> {
        return (2 until max step 2).toList()
    }

    /**
     * Lấy danh sách số lẻ nhỏ hơn max
     * Ví dụ: max=10 → [1, 3, 5, 7, 9]
     */
    private fun getOddNumbers(max: Int): List<Int> {
        return (1 until max step 2).toList()
    }

    /**
     * Lấy danh sách số chính phương nhỏ hơn max
     * Số chính phương: 1²=1, 2²=4, 3²=9, 4²=16, 5²=25, ...
     * Ví dụ: max=30 → [1, 4, 9, 16, 25]
     */
    private fun getSquareNumbers(max: Int): List<Int> {
        val result = mutableListOf<Int>()
        var i = 1

        // Tính i² cho đến khi i² >= max
        while (i * i < max) {
            result.add(i * i)
            i++
        }

        return result
    }

    /**
     * Lấy danh sách số nguyên tố nhỏ hơn max
     * Số nguyên tố: chỉ chia hết cho 1 và chính nó
     * Ví dụ: max=20 → [2, 3, 5, 7, 11, 13, 17, 19]
     */
    private fun getPrimeNumbers(max: Int): List<Int> {
        if (max <= 2) return emptyList()

        val result = mutableListOf<Int>()

        // Kiểm tra từng số từ 2 đến max-1
        for (num in 2 until max) {
            if (isPrime(num)) {
                result.add(num)
            }
        }

        return result
    }

    /**
     * Kiểm tra xem n có phải số nguyên tố không
     */
    private fun isPrime(n: Int): Boolean {
        if (n < 2) return false           // Số < 2 không phải số nguyên tố
        if (n == 2) return true            // 2 là số nguyên tố
        if (n % 2 == 0) return false       // Số chẵn (trừ 2) không phải số nguyên tố

        // Chỉ cần kiểm tra đến √n
        val sqrt = kotlin.math.sqrt(n.toDouble()).toInt()
        for (i in 3..sqrt step 2) {
            if (n % i == 0) return false   // Nếu chia hết → không phải số nguyên tố
        }

        return true
    }

    /**
     * Lấy danh sách số hoàn hảo nhỏ hơn max
     * Số hoàn hảo: Tổng các ước số (không kể chính nó) = chính số đó
     * Ví dụ: 6 = 1 + 2 + 3
     *        28 = 1 + 2 + 4 + 7 + 14
     * Ví dụ: max=30 → [6, 28]
     */
    private fun getPerfectNumbers(max: Int): List<Int> {
        val result = mutableListOf<Int>()

        for (num in 2 until max) {
            if (isPerfectNumber(num)) {
                result.add(num)
            }
        }

        return result
    }

    /**
     * Kiểm tra xem n có phải số hoàn hảo không
     */
    private fun isPerfectNumber(n: Int): Boolean {
        if (n < 2) return false

        var sum = 1  // Bắt đầu với 1 (luôn là ước số)

        // Chỉ cần kiểm tra đến √n để tìm ước số
        val sqrt = kotlin.math.sqrt(n.toDouble()).toInt()

        for (i in 2..sqrt) {
            if (n % i == 0) {
                sum += i              // Thêm ước số i
                if (i != n / i) {     // Nếu i khác n/i
                    sum += n / i      // Thêm ước số n/i
                }
            }
        }

        // Số hoàn hảo: tổng ước = chính nó
        return sum == n
    }

    /**
     * Lấy danh sách số Fibonacci nhỏ hơn max
     * Dãy Fibonacci: 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, ...
     * Công thức: F(n) = F(n-1) + F(n-2), với F(1) = F(2) = 1
     * Ví dụ: max=50 → [1, 1, 2, 3, 5, 8, 13, 21, 34]
     */
    private fun getFibonacciNumbers(max: Int): List<Int> {
        val result = mutableListOf<Int>()
        var a = 1  // F(n-1)
        var b = 1  // F(n)

        // Tạo dãy Fibonacci cho đến khi >= max
        while (a < max) {
            result.add(a)

            // Tính số Fibonacci tiếp theo
            val temp = a + b  // F(n+1) = F(n) + F(n-1)
            a = b             // Dịch chuyển: a = F(n)
            b = temp          // Dịch chuyển: b = F(n+1)
        }

        return result
    }
}