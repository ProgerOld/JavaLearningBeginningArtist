package com.example.javalearningbeginningartist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawingView drawingView; // создание поля созданного нами класса
    private ImageButton currPaint; // создание поля кнопки цвета краски в палитре
    private ImageButton drawButton; // создание поля кнопки кисти рисования
    private ImageButton eraseButton; // добавим поле кнопки ластика
    private ImageButton newButton; // добавим поле кнопки нового рисунка
    private ImageButton saveButton; // добавим поле кнопки сохранения
    private float smallBrush, mediumBrush, largeBrush; // поля размеров кистей

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // присваивание полям соответствующих id
        drawingView = findViewById(R.id.drawing);
        LinearLayout paintLayout = findViewById(R.id.paint_colors); // извлечение переменной контейнера по id

        // присваивание дополнительным полям соответствующих id
        smallBrush = getResources().getInteger(R.integer.small_size); //маленькая кисть
        mediumBrush = getResources().getInteger(R.integer.medium_size); //средняя кисть
        largeBrush = getResources().getInteger(R.integer.large_size); //большая кисть
        drawButton = findViewById(R.id.fabBrush); //Кисть
        eraseButton = findViewById(R.id.fabErase); //Стереть
        newButton = findViewById(R.id.fabAdd); // Новый рисунок
        saveButton = findViewById(R.id.fabSave); //Сохранить рисунок

        currPaint = (ImageButton) paintLayout.getChildAt(0); // получение первой кнопки
        currPaint.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.paint_pressed)); // при выборе данной кнопки она должна изменить вид в соответствии с ресурсом

        drawButton.setOnClickListener(this); //Обработка нажатия кнопки выбор кисти
        eraseButton.setOnClickListener(this); //Обработка нажатия кнопки стереть
        newButton.setOnClickListener(this); //Обработка нажатия кнопки новый рисунок
        saveButton.setOnClickListener(this); //Обработка нажатия кнопки сохранить

    }

    // метод выбора цвета
    public void paintClicked(View view){

        drawingView.setErase(false); // задание выбора кисти
        drawingView.setBrushSize(drawingView.getLastBrushSize());  // установление размера кисти до использования ластика

        // проверка выбранного цвета кнопки
        if(view != currPaint){
            // извлечение тега кнопки
            ImageButton imgView = (ImageButton) view;
            String color = view.getTag().toString();

            // задание цвета по извлечённому тегу кнопки
            drawingView.setColor(color);

            // обновление пользовательского интерфейса
            imgView.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.paint_pressed)); // присваивание новой формы кнопке (формы выбора)
            currPaint.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.paint)); // возврат прошлой кнопке обычной формы
            currPaint = (ImageButton) view; // присваивание кнопки обратно view
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.fabBrush){
            //Действие при нажатии кисти
            final Dialog brushDialog = new Dialog(this, R.style.Dialog); // создадим диалоговое окно выбора размера кисти
            brushDialog.setTitle("Размер кисти: "); // зададим заголовок диалогового окна
            brushDialog.setContentView(R.layout.brush_chooser); // добавим ресурс диалогового окна
            drawingView.setBrushSize(mediumBrush); // зададим средний размер кисти по умолчанию

            ImageButton smallBtn = brushDialog.findViewById(R.id.small_brush); // создание кнопки маленького размера кисти
            // добавим слушателя кнопки задания маленького размера кисти
            smallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawingView.setErase(false); // задание выбора кисти
                    drawingView.setBrushSize(smallBrush); // задание малого размера кисти
                    drawingView.setLastBrushSize(smallBrush); // задание последнего малого размера кисти
                    brushDialog.dismiss(); // закрытие диалога
                }
            });

            ImageButton mediumBtn = brushDialog.findViewById(R.id.medium_brush); // создание кнопки среднего размера кисти
            // добавим слушателя кнопки задания среднего размера кисти
            mediumBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawingView.setErase(false); // задание выбора кисти
                    drawingView.setBrushSize(mediumBrush); // задание среднего размера кисти
                    drawingView.setLastBrushSize(mediumBrush); // задание последнего среднего размера кисти
                    brushDialog.dismiss(); // закрытие диалога
                }
            });

            ImageButton largeBtn = brushDialog.findViewById(R.id.large_brush); // создание кнопки большого размера кисти
            // добавим слушателя кнопки задания большого размера кисти
            largeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawingView.setErase(false); // задание выбора кисти
                    drawingView.setBrushSize(largeBrush); // задание большого размера кисти
                    drawingView.setLastBrushSize(largeBrush); // задание последнего большого размера кисти
                    brushDialog.dismiss(); // закрытие диалога
                }
            });

            brushDialog.show(); // отображение на экране данного диалога

        } else if (v.getId() == R.id.fabErase) {
            //Действие при нажатии ластика
            final Dialog brushDialog = new Dialog(this, R.style.Dialog); // создадим диалоговое окно выбора размера кисти
            brushDialog.setTitle("Размер ластика: "); // зададим заголовок диалогового окна
            brushDialog.setContentView(R.layout.brush_chooser); // добавим ресурс диалогового окна
            drawingView.setBrushSize(mediumBrush); // зададим средний размер кисти по умолчанию

            ImageButton smallBtn = brushDialog.findViewById(R.id.small_brush); // создание кнопки маленького размера кисти
            // добавим слушателя кнопки задания маленького размера кисти
            smallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawingView.setErase(true); // задание выбора кисти
                    drawingView.setBrushSize(smallBrush); // задание малого размера кисти
                    drawingView.setLastBrushSize(smallBrush); // задание последнего малого размера кисти
                    brushDialog.dismiss(); // закрытие диалога
                }
            });

            ImageButton mediumBtn = brushDialog.findViewById(R.id.medium_brush); // создание кнопки среднего размера кисти
            // добавим слушателя кнопки задания среднего размера кисти
            mediumBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawingView.setErase(true); // задание выбора кисти
                    drawingView.setBrushSize(mediumBrush); // задание среднего размера кисти
                    drawingView.setLastBrushSize(mediumBrush); // задание последнего среднего размера кисти
                    brushDialog.dismiss(); // закрытие диалога
                }
            });

            ImageButton largeBtn = brushDialog.findViewById(R.id.large_brush); // создание кнопки большого размера кисти
            // добавим слушателя кнопки задания большого размера кисти
            largeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawingView.setErase(true); // задание выбора кисти
                    drawingView.setBrushSize(largeBrush); // задание большого размера кисти
                    drawingView.setLastBrushSize(largeBrush); // задание последнего большого размера кисти
                    brushDialog.dismiss(); // закрытие диалога
                }
            });

            brushDialog.show(); // отображение на экране данного диалога

        }else if (v.getId() == R.id.fabAdd) {
            //Действие при нажатии новый документ
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this); // создание диалогового окна типа AlertDialog
            newDialog.setTitle("Новый рисунок"); // заголовок диалогового окна
            newDialog.setMessage("Новый рисунок (имеющийся будет удалён)?"); // сообщение диалога
            newDialog.setPositiveButton("Да", new DialogInterface.OnClickListener(){ // пункт выбора "да"
                public void onClick(DialogInterface dialog, int which){
                    drawingView.startNew(); // старт нового рисунка
                    dialog.dismiss(); // закрыть диалог
                }
            });
            newDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){ // пункт выбора "нет"
                    dialog.cancel(); // выход из диалога
                }
            });
            newDialog.show(); // отображение на экране данного диалога

        }else if (v.getId() == R.id.fabSave) {
            //Действие при нажатии сохранить
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this); // создание диалогового окна типа AlertDialog
            newDialog.setTitle("Сохранить"); // заголовок диалогового окна
            newDialog.setMessage("Сохранить рисунок?"); // сообщение диалога
            newDialog.setPositiveButton("Да", new DialogInterface.OnClickListener(){ // пункт выбора "да"
                public void onClick(DialogInterface dialog, int which){
                    //Код сохранения
                    drawingView.setDrawingCacheEnabled(true); //сохраним кэш имеющегося рисунка

                    // сохранение изображения в файл
                    // (метод insertImage() записывает изображение в постоянную память устройства,
                    // UUID.randomUUID().toString() - генерирует случайную строку для названия имени файла)
                    String imageSaved = MediaStore.Images.Media.insertImage(
                            getContentResolver(), drawingView.getDrawingCache(),
                            UUID.randomUUID().toString()+".png", "drawing");

                    // вывод тоста информации о сохранении рисунка
                    if(imageSaved != null) { // если изображение сохранено, то вывод тоста об успешности сохранения
                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                "Изображение успешно сохранено в галлерею!", Toast.LENGTH_SHORT);
                        savedToast.show();
                    } else { // иначе, вывод тоста об неудачном сохранении
                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                "Сохранить изображение не удалось!", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    }

                    drawingView.destroyDrawingCache(); // удаление кэша рисунка

                }
            });
            newDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){ // пункт выбора "нет"
                    dialog.cancel(); // выход из диалога
                }
            });
            newDialog.show(); // отображение на экране данного диалога
        }


    } //onClick()

}