package vn.edu.hust.studentman

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class StudentAdapter(val students: MutableList<StudentModel>): RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
  class StudentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val textStudentName: TextView = itemView.findViewById(R.id.text_student_name)
    val textStudentId: TextView = itemView.findViewById(R.id.text_student_id)
    val imageEdit: ImageView = itemView.findViewById(R.id.image_edit)
    val imageRemove: ImageView = itemView.findViewById(R.id.image_remove)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
    val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_student_item,
       parent, false)
    return StudentViewHolder(itemView)
  }

  override fun getItemCount(): Int = students.size

  override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
    val student = students[position]

    holder.textStudentName.text = student.studentName
    holder.textStudentId.text = student.studentId
    holder.imageEdit.setOnClickListener {
      val dialogView = LayoutInflater.from(it.context).inflate(R.layout.dialog_add_edit_student, null)
      val dialog = AlertDialog.Builder(it.context)
        .setTitle("Chỉnh sửa sinh viên")
        .setView(dialogView)
        .setPositiveButton("Lưu") { _, _ ->
          val newName = dialogView.findViewById<EditText>(R.id.edit_student_name).text.toString()
          val newId = dialogView.findViewById<EditText>(R.id.edit_student_id).text.toString()
          if (newName.isNotBlank() && newId.isNotBlank()) {
            students[position] = StudentModel(newName, newId)
            notifyItemChanged(position)
          }
        }
        .setNegativeButton("Hủy", null)
        .create()

      dialogView.findViewById<EditText>(R.id.edit_student_name).setText(student.studentName)
      dialogView.findViewById<EditText>(R.id.edit_student_id).setText(student.studentId)
      dialog.show()
    }
    holder.imageRemove.setOnClickListener {
      val context = it.context
      val studentToRemove = students[position]
      AlertDialog.Builder(context)
        .setTitle("Xóa sinh viên")
        .setMessage("Bạn có chắc chắn muốn xóa ${studentToRemove.studentName}?")
        .setPositiveButton("Xóa") { _, _ ->
          students.removeAt(position)
          notifyItemRemoved(position)

          Snackbar.make(it, "${studentToRemove.studentName} đã bị xóa.", Snackbar.LENGTH_LONG)
            .setAction("Hoàn tác") {
              students.add(position, studentToRemove)
              notifyItemInserted(position)
            }
            .show()
        }
        .setNegativeButton("Hủy", null)
        .show()
    }

  }
}