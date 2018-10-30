package com.example.android.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    String equation = "";
    String ans = "";


    public void one(View view){
        equation += "1";
        display(equation);
    }

    public void two(View view){
        equation += "2";
        display(equation);
    }

    public void three(View view){
        equation += "3";
        display(equation);
    }

    public void four(View view){
        equation += "4";
        display(equation);
    }

    public void five(View view){
        equation += "5";
        display(equation);
    }

    public void six(View view){
        equation += "6";
        display(equation);
    }

    public void seven(View view){
        equation += "7";
        display(equation);
    }

    public void eight(View view){
        equation += "8";
        display(equation);
    }

    public void nine(View view){
        equation += "9";
        display(equation);
    }

    public void zero(View view){
        equation += "0";
        display(equation);
    }

    public void subtract(View view){
        equation += "-";
        display(equation);
    }

    public void addition(View view){
        equation += "+";
        display(equation);
    }

    public void multiply(View view){
        equation += "*";
        display(equation);
    }

    public void divide(View view){
        equation += "/";
        display(equation);
    }

    public void decimal(View view){
        equation += ".";
        display(equation);
    }

    public void clear(View view){
        equation = "";
        display(equation);
        ans = "";
        displayAnswer(ans);
    }

    public void equal(View view) {
        if(equation != "") {
            char c = equation.charAt(0);
            char l = equation.charAt(equation.length() - 1);

            if (c == '/' || c == '*' || c == '-' || c == '+') {
                ans = "Invalid";
            } else if (l == '/' || l == '*' || l == '-' || l == '+') {
                ans = "Invalid";
            } else if (c == l && c == '.') {
                ans = "Invalid";
            } else {
                ans = "" + eval(equation);
            }
            displayAnswer(ans);
        }
    }

    private void display(String number) {
        TextView quantityTextView = (TextView) findViewById(R.id.line1);
        quantityTextView.setText("" + number);
    }

    private void displayAnswer(String number){
        TextView quantityTextView = (TextView) findViewById(R.id.line2);
        quantityTextView.setText("" + number);
    }

    private static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

}
