package mykyta.titarenko.labtask4;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateAndEditNoteActivity extends AppCompatActivity {
    ImageView iconImageView;
    List<Note> notes;
    byte[] imageBytes;
    Notes Notes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_and_edit_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Notes = ((NotesApplication) getApplication()).GetNotes();
        Button createOrEditButton = findViewById(R.id.createOrEditButton);
        EditText titleEditTextButton = findViewById(R.id.titleEditText);
        EditText descriptionEditTextButton = findViewById(R.id.descriptionEditText);
        Spinner importanceSpinner = findViewById(R.id.importanceSpinner);
        List<ImportanceItem> importanceItems = new ArrayList<>();
        importanceItems.add(new ImportanceItem(getString(R.string.lowImportance), R.drawable.not_important_icon));
        importanceItems.add(new ImportanceItem(getString(R.string.mediumImportance), R.drawable.important_icon));
        importanceItems.add(new ImportanceItem(getString(R.string.highImportance), R.drawable.very_important_icon));
        ImportanceAdapter importanceAdapter = new ImportanceAdapter(CreateAndEditNoteActivity.this, importanceItems);
        importanceSpinner.setAdapter(importanceAdapter);
        EditText editTextDate = findViewById(R.id.editTextDate);
        EditText editTextTime = findViewById(R.id.editTextTime);
        Button chooseImageButton = findViewById(R.id.chooseImageButton);
        Button cancleButton = findViewById(R.id.cancleButton);
        iconImageView = findViewById(R.id.iconImageView);
        notes = Notes.getNotes();

        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        if (getIntent().getBooleanExtra("view", false) || getIntent().getBooleanExtra("edit", false)){
            Note note = notes.get(getIntent().getIntExtra("index", 0));
            importanceSpinner.setSelection(note.getImportance());
            titleEditTextButton.setText(note.getTitle());
            descriptionEditTextButton.setText(note.getDescription());
            editTextDate.setText(note.getEventDate());
            editTextTime.setText(note.getEventTime());
            imageBytes = NoteAdapter.StandartIconCheck(note.getImageData(), this);
            iconImageView.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
            if (getIntent().getBooleanExtra("view", false)){
                titleEditTextButton.setEnabled(false);
                descriptionEditTextButton.setEnabled(false);
                descriptionEditTextButton.setHint("");
                editTextDate.setEnabled(false);
                editTextDate.setHint("");
                editTextTime.setEnabled(false);
                editTextTime.setHint("");
                removeTransparency(titleEditTextButton);
                removeTransparency(descriptionEditTextButton);
                removeTransparency(editTextDate);
                removeTransparency(editTextTime);
                chooseImageButton.setVisibility(View.INVISIBLE);
                importanceSpinner.setEnabled(false);
                createOrEditButton.setVisibility(View.INVISIBLE);
            } else {
                createOrEditButton.setText(getString(R.string.edit));
            }
        }

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });

        createOrEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleEditTextButton.getText().toString().isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateAndEditNoteActivity.this);
                    builder.setTitle(R.string.incorrectInput);
                    builder.setMessage(R.string.noTitle);
                    builder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create().show();
                    return;
                }

                if (getIntent().getBooleanExtra("edit", false)){
                    Note note = notes.get(getIntent().getIntExtra("index", 0));
                    Notes.EditNote( note.getNumber(),
                            titleEditTextButton.getText().toString(),
                            descriptionEditTextButton.getText().toString(),
                            importanceSpinner.getSelectedItemPosition(),
                            editTextDate.getText().toString(),
                            editTextTime.getText().toString(),
                            note.getCreationDate(),
                            imageBytes);
                } else {
                    Notes.AddNote(titleEditTextButton.getText().toString(),
                            descriptionEditTextButton.getText().toString(),
                            importanceSpinner.getSelectedItemPosition(),
                            editTextDate.getText().toString(),
                            editTextTime.getText().toString(),
                            dayOfMonth + "." + month + "." + year,
                            imageBytes);
                }
                setResult(RESULT_OK);
                finish();
            }
        });
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateAndEditNoteActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextDate.setText(dayOfMonth + "." + month + "." + year);
                    }
                }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });
        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateAndEditNoteActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        editTextTime.setText(hourOfDay + ":" + minute);
                    }
                }, hourOfDay, minute, true);
                timePickerDialog.show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK){
            iconImageView.setImageURI(data.getData());
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imageBytes = stream.toByteArray();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void removeTransparency(TextView textView){
        int currentColor = textView.getCurrentTextColor();

        int newColor = (currentColor & 0x00FFFFFF) | (255 << 24);

        textView.setTextColor(newColor);
    }
}

