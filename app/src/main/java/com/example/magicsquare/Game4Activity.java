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

public class Game4Activity extends AppCompatActivity implements OnClickListener {

    private static final int MIN_RANGE = 34;
    private static final int MAX_RANGE = 100;
    private static final int BUTTON_NEW_GAME = R.id.newgame;
    private static final int BUTTON_BACK = R.id.back;
    private static final int BUTTON_VALIDATE = R.id.validate;
    private static final int LAYOUT_ACTIVITY = R.layout.activity_game4;
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

        EditText[] cells = new EditText[16];

        for (int i = 0; i < cells.length; i++) {
            int viewId = getResources().getIdentifier("cell" + (i + 1), "id", getPackageName());
            cells[i] = findViewById(viewId);
        }

        TextView sumValue = findViewById(SUM_VALUE);

        initialMatrix = new int[4][4];
        resultMatrix = new int[4][4];
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
        while (randomIndices.size() < 6) {
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
        matrix[3][3] = combination[3];

        combination = generateCombination(totalSum, numbersList);
        matrix[0][3] = combination[0];
        matrix[1][2] = combination[1];
        matrix[2][1] = combination[2];
        matrix[3][0] = combination[3];

        iterationsLeft = processMatrixRow(matrix, 0, numbersList, iterationsLeft);
        iterationsLeft = processMatrixColumn(matrix, 0, numbersList, iterationsLeft);
        iterationsLeft = processMatrixColumn(matrix, 3, numbersList, iterationsLeft);
        iterationsLeft = processMatrixRow(matrix, 3, numbersList, iterationsLeft);

        if (iterationsLeft == 1) {
            generateMatrix(matrix);
        }
    }

    private static int processMatrixRow(int[][] matrix, int row, List<Integer> numbersList, int iterationsLeft) {
        while (iterationsLeft > 1) {
            iterationsLeft--;
            matrix[row][1] = numbersList.get(random.nextInt(numbersList.size()));
            matrix[row][2] = numbersList.get(random.nextInt(numbersList.size()));
            if (sum(matrix[row]) == totalSum) {
                numbersList.remove(Integer.valueOf(matrix[row][1]));
                numbersList.remove(Integer.valueOf(matrix[row][2]));
                break;
            }
        }
        return iterationsLeft;
    }

    private static int processMatrixColumn(int[][] matrix, int column, List<Integer> numbersList, int iterationsLeft) {
        while (iterationsLeft > 1) {
            iterationsLeft--;
            matrix[1][column] = numbersList.get(random.nextInt(numbersList.size()));
            matrix[2][column] = numbersList.get(random.nextInt(numbersList.size()));
            if (matrix[0][column] + matrix[1][column] + matrix[2][column] + matrix[3][column] == totalSum) {
                numbersList.remove(Integer.valueOf(matrix[1][column]));
                numbersList.remove(Integer.valueOf(matrix[2][column]));
                break;
            }
        }
        return iterationsLeft;
    }

    private static int[] generateCombination(int targetSum, List<Integer> numbers) {
        int[] combination = new int[4];

        while (sum(combination) != targetSum) {
            for (int i = 0; i < combination.length; i++) {
                combination[i] = numbers.get(random.nextInt(numbers.size()));
            }
        }

        for (int value : combination) {
            numbers.remove(Integer.valueOf(value));
        }

        return combination;
    }

    private static int[] genCom(int targetSum, List<Integer> sum) {
        int[] combination = new int[2];

        while (sum(combination) + initialMatrix[0][0] + initialMatrix[0][3] != targetSum) {
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

        EditText[] cells = new EditText[16];
        int[] cellValues = new int[16];

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