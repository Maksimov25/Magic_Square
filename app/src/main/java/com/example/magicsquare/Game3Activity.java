package com.example.magicsquare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game3Activity extends AppCompatActivity implements OnClickListener {

    private static final int MIN_RANGE = 12;
    private static final int MAX_RANGE = 100;
    private static final int BUTTON_NEW_GAME = R.id.newgame;
    private static final int BUTTON_BACK = R.id.back;
    private static final int BUTTON_VALIDATE = R.id.validate;
    private static final int LAYOUT_ACTIVITY = R.layout.activity_game3;
    private static final int SUM_VALUE = R.id.sumValue;
    private static int[][] initialMatrix;
    private static int[][] resultMatrix;
    private static Random random;
    private static int totalSum;
    private Button backButton;
    private Button resetButton;

    private Button validateButton;

    public void onClick(View v) {
        if (v == backButton) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (v.getId() == BUTTON_NEW_GAME) {
            initializeGame();
        } else if (v.getId() == BUTTON_VALIDATE) {
            initializeGame();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT_ACTIVITY);

        initializeUI();
        setupButtonListeners();

        random = new Random();

        initializeGame();
    }

    private void initializeUI() {
        resetButton = findViewById(BUTTON_NEW_GAME);
        backButton = findViewById(BUTTON_BACK);
        validateButton = findViewById(BUTTON_VALIDATE);
    }

    private void setupButtonListeners() {
        resetButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        validateButton.setOnClickListener(v -> validateMatrix());
    }


    public void initializeGame() {

        EditText[] cells = new EditText[9];

        for (int i = 0; i < cells.length; i++) {
            int viewId = getResources().getIdentifier("cell" + (i + 1), "id", getPackageName());
            cells[i] = findViewById(viewId);
        }

        TextView sumValue = findViewById(SUM_VALUE);

        initialMatrix = new int[3][3];
        resultMatrix = new int[3][3];
        generateMatrix(initialMatrix);

        String desiredSumString = getString(R.string.desired_sum, String.valueOf(totalSum));
        sumValue.setText(desiredSumString);

        for (int i = 0; i < initialMatrix.length; i++) {
            System.arraycopy(initialMatrix[i], 0, resultMatrix[i], 0, initialMatrix[i].length);
        }

        List<int[]> indices = new ArrayList<>();

        for (int i = 0; i < initialMatrix.length; i++) {
            for (int j = 0; j < initialMatrix[i].length; j++) {
                indices.add(new int[]{i, j});
            }
        }

        List<int[]> randomIndices = new ArrayList<>();
        while (randomIndices.size() < 4) {
            int[] index = indices.get(random.nextInt(indices.size()));
            if (!randomIndices.contains(index)) {
                randomIndices.add(index);
            }
        }

        for (int[] index : randomIndices) {
            initialMatrix[index[0]][index[1]] = 0;
        }


        int index = 0;
        for (int[] matrix : initialMatrix) {
            for (int i : matrix) {
                cells[index].setText(String.valueOf(i));
                index++;
            }
        }


    }

    private static void generateMatrix(int[][] matrix) {
        int iterationsLeft = 200;
        totalSum = getRandomNumberInRange();

        List<Integer> numbersList = new ArrayList<>();
        for (int num = 0; num < totalSum - 3; num++) {
            numbersList.add(num);
        }

        int[] combination = generateCombination(totalSum, numbersList);
        matrix[0][0] = combination[0];
        matrix[1][1] = combination[1];
        matrix[2][2] = combination[2];

        combination = genCom(totalSum, numbersList);
        matrix[0][2] = combination[0];
        matrix[2][0] = combination[1];

        iterationsLeft = processMatrixRow(matrix, 1, 2, numbersList, iterationsLeft);
        iterationsLeft = processMatrixRow(matrix, 0, 1, numbersList, iterationsLeft);
        iterationsLeft = processMatrixColumn(matrix, 1, 0, numbersList, iterationsLeft);
        iterationsLeft = processMatrixColumn(matrix, 2, 1, numbersList, iterationsLeft);

        if (iterationsLeft == 1) {
            iterationsLeft = 200;
            generateMatrix(matrix);
        }
    }

    private static int processMatrixRow(int[][] matrix, int row, int col, List<Integer> numbersList, int iterationsLeft) {
        while (iterationsLeft > 1) {
            iterationsLeft--;
            matrix[row][col] = numbersList.get(random.nextInt(numbersList.size()));
            if (sum(getRow(matrix, row)) == totalSum) {
                numbersList.remove(Integer.valueOf(matrix[row][col]));
                break;
            }
        }
        return iterationsLeft;
    }

    private static int processMatrixColumn(int[][] matrix, int row, int col, List<Integer> numbersList, int iterationsLeft) {
        while (iterationsLeft > 1) {
            iterationsLeft--;
            matrix[row][col] = numbersList.get(random.nextInt(numbersList.size()));
            if (sum(getColumn(matrix, col)) == totalSum) {
                numbersList.remove(Integer.valueOf(matrix[row][col]));
                break;
            }
        }
        return iterationsLeft;
    }

    private static int[] getRow(int[][] matrix, int row) {
        return matrix[row];
    }

    private static int[] getColumn(int[][] matrix, int col) {
        int[] column = new int[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            column[i] = matrix[i][col];
        }
        return column;
    }

    private static int[] generateCombination(int targetSum, List<Integer> mas) {
        int[] combination = new int[3];

        while (sum(combination) != targetSum) {
            for (int i = 0; i < combination.length; i++) {
                combination[i] = mas.get(random.nextInt(mas.size()));
            }
        }

        for (int value : combination) {
            mas.remove(Integer.valueOf(value));
        }

        return combination;
    }

    private static int[] genCom(int targetSum, List<Integer> sum) {
        int[] combination = new int[2];

        while (sum(combination) + initialMatrix[1][1] != targetSum) {
            for (int i = 0; i < combination.length; i++) {
                combination[i] = sum.get(random.nextInt(sum.size()));
            }
        }

        for (int value : combination) {
            sum.remove(Integer.valueOf(value));
        }

        return combination;
    }

    private static int sum(int[] array) {
        int sum = 0;
        for (int value : array) {
            sum += value;
        }
        return sum;
    }

    private static int getRandomNumberInRange() {
        Random random = new Random();
        return random.nextInt((MAX_RANGE - MIN_RANGE) + 1) + MIN_RANGE;
    }

    public void validateMatrix() {

        EditText[] cells = new EditText[9];
        int[] cellValues = new int[9];

        for (int i = 0; i < 9; i++) {
            cells[i] = findViewById(getResources().getIdentifier("cell" + (i + 1), "id", getPackageName()));
            cellValues[i] = Integer.parseInt(cells[i].getText().toString());
        }

        int index = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                initialMatrix[row][col] = cellValues[index];
                index++;
            }
        }
        boolean isMagicSquare = true;

        for (int i = 0; i < initialMatrix.length; i++) {
            for (int j = 0; j < initialMatrix[i].length; j++) {
                if (initialMatrix[i][j] != resultMatrix[i][j]) {
                    isMagicSquare = false;
                    break;
                }
            }
            if (!isMagicSquare) {
                break;
            }
        }


        if (isMagicSquare) {
            Toast.makeText(this, getString(R.string.magic_square), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.not_magic_square), Toast.LENGTH_SHORT).show();
        }
    }


}