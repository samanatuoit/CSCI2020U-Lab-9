package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Main extends Application {

    private Canvas canvas;
    private File inFile;
    private Group root = new Group();

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        //Group root = new Group();
        canvas = new Canvas();
        canvas.widthProperty().bind(primaryStage.widthProperty());
        canvas.heightProperty().bind(primaryStage.heightProperty());
        root.getChildren().add(canvas);

        primaryStage.setTitle("Lab 9");
        Scene scene = new Scene(root, 1000, 950);
        primaryStage.setScene(scene);
        primaryStage.show();

        downloadStockPrices();
    }

    private void downloadStockPrices() {
        ArrayList<Double> stockOnePrices = new ArrayList<>();
        ArrayList<Double> stockTwoPrices = new ArrayList<>();

        try {
            //Socket socket = new Socket("http://ichart.finance.yahoo.com/table.csv?s=GOOG&a=1&b=01&c=2010&d=11&e=31&f=2015&g=m", 80);
            URL urlStockOne = new URL("http://ichart.finance.yahoo.com/table.csv?s=GOOG&a=1&b=01&c=2010&d=11&e=31&f=2015&g=m");
            URLConnection connStockOne = urlStockOne.openConnection();
            connStockOne.setDoOutput(false);
            connStockOne.setDoInput(true);


            // That link sends a file. Write the contents of that file to a new file on our own local machine.
            BufferedReader in = new BufferedReader(new InputStreamReader(connStockOne.getInputStream()));
            String line;
            /*inFile = new File("stockOne.csv");
            PrintWriter fout = new PrintWriter(inFile);
            while ((line = in.readLine()) != null) {
                fout.println(line);
            }
            fout.close();*/

            while ((line = in.readLine()) != null) {
                String[] data = line.split(",");
                if (!data[4].equals("Close")) {

                    //System.out.println(data[4]);
                    stockOnePrices.add(Double.parseDouble(data[4]));
                }
            }

            URL urlStockTwo = new URL("http://ichart.finance.yahoo.com/table.csv?s=AAPL&a=1&b=01&c=2010&d=11&e=31&f=2015&g=m");
            URLConnection connStockTwo = urlStockTwo.openConnection();
            connStockTwo.setDoOutput(false);
            connStockTwo.setDoInput(true);

            in = new BufferedReader(new InputStreamReader(connStockTwo.getInputStream()));


            while ((line = in.readLine()) != null) {
                String[] data = line.split(",");
                if (!data[4].equals("Close")) {

                    stockTwoPrices.add(Double.parseDouble(data[4]));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        drawPlotLine(root, stockOnePrices, stockTwoPrices);

    }
    private void drawPlotLine(Group root, ArrayList<Double> stockOnePrices, ArrayList<Double> stockTwoPrices) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        /*
        Parameters:
        x1 - the X coordinate of the starting point of the line.
        y1 - the Y coordinate of the starting point of the line.
        x2 - the X coordinate of the ending point of the line.
        y2 - the Y coordinate of the ending point of the line.
         */

        gc.setStroke(Color.BLACK);
        // y-axis line
        gc.strokeLine(50,50,50,900);
        // x-axis line
        gc.strokeLine(50,900,950,900);

        gc.setStroke(Color.RED);
        plotLine(stockOnePrices, gc);
        gc.setStroke(Color.BLUE);
        plotLine(stockTwoPrices, gc);

    }

    private void plotLine(ArrayList<Double> stockPrices, GraphicsContext gc) {

        double baseX = 50;
        double baseY = 650;
        double oldPrice = stockPrices.get(0);
        double newPrice;
        double oldDate = baseX;
        double newDate = baseX;


        for (int i = 1; i < stockPrices.size(); i++) {
            double price = stockPrices.get(i);

            //price / 100 * 50
            newPrice = baseY - ((price / 100) * 50);
            newDate += 10;
            gc.strokeLine(oldDate, oldPrice, newDate, newPrice);
            oldDate = newDate;
            oldPrice = newPrice;
            //System.out.println("newPrice = " + newPrice);
        }



    }


    public static void main(String[] args) {
        launch(args);
    }
}
