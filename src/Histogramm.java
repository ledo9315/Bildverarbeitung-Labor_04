import java.util.*;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

public class Histogramm {
    int histSize = 256;
    int breite = 512;
    int hoehe = 400;
    float[] range = {0, 256};
    Scalar hintergrund = new Scalar(255, 255, 255);
    Scalar vordergrund = new Scalar(0, 0, 0);
    Mat histogrammMatrix;

    public void generiere(Mat bildMatrix) {

        // das Bild in drei RGB-Kanäle verteilen.
        List<Mat> bgr = new ArrayList<>();
        Core.split(bildMatrix, bgr);

        // Erstelle eine Matrix, um das Histogramm-Bild nur für Kanal_0 dort zu speichern
        histogrammMatrix = gibHistogrammMatrix(bgr, 0);
    }

    public Mat gibHistogrammMatrix(List<Mat> bgr, int kanal) {
        Mat matrix = new Mat();
        Mat histImage = new Mat(hoehe, breite, CvType.CV_8UC1, hintergrund);

        // Setze die Werte zwischen 0 und 255
        MatOfFloat histRange = new MatOfFloat(range);

        // Rechne die Histogramm-Werte
        Imgproc.calcHist(bgr, new MatOfInt(kanal), new Mat(), matrix, new MatOfInt(histSize), new MatOfFloat(histRange), false);

        // Gib Feldern eine ähnliche Größe
        int pixelBreite = (int) Math.round((double) breite / histSize);

        // Vor dem Zeichnen muss Histogramm normalisiert werden.
        Core.normalize(matrix, matrix, 0, histImage.rows(), Core.NORM_MINMAX);

        // matrix in einem 1D-Array eintragen
        float[] histData = new float[(int) (matrix.total() * matrix.channels())];
        matrix.get(0, 0, histData);

        // zeichnen
        for (int i = 1; i < histSize; i++) {
            Imgproc.line(histImage, new Point(pixelBreite * (i - 1), hoehe - Math.round(histData[i - 1])),
                    new Point(pixelBreite * (i), hoehe - Math.round(histData[i])), vordergrund, 2);
        }
        return histImage;
    }

    public void showMittelWertUndAbweichung(Mat bildMatrix, int kanal) {
        MatOfDouble mittelwert = new MatOfDouble();
        MatOfDouble abweichung = new MatOfDouble();
        Core.meanStdDev(bildMatrix, mittelwert, abweichung);
        double[] mittelwertKanaele = mittelwert.get(0, 0);
        double[] abweichungKanaele = abweichung.get(0, 0);
        System.out.println("Mittelwert:" + mittelwertKanaele[kanal]);
        System.out.println("Abweichung:" + abweichungKanaele[kanal]);
    }

    public int[] gibHistogramm(Mat bildMatrix) {
        int[] histogramm = new int[256];

        for (int i = 0; i < bildMatrix.rows(); i++) {
            for (int j = 0; j < bildMatrix.cols(); j++) {
                double[] data = bildMatrix.get(i, j);
                histogramm[(int) data[0]]++;
            }
        }
        return histogramm;
    }

    public float[] gibNormalisierteLUT(int[] histogramm, int anzahlPixel) {
        float[] lut = new float[256];
        int sum = 0;

        for (int i = 0; i < histogramm.length; i++) {
            sum += histogramm[i];
            lut[i] = sum * 255f / anzahlPixel;
            System.out.println("lut[" + i + "]=" + lut[i]);
        }
        return lut;
    }

    public Mat gibAequalisierteMatrix(float[] lut, Mat bildMatrix) {
        Mat ausgabe = new Mat(bildMatrix.rows(), bildMatrix.cols(), bildMatrix.type());

        for (int i = 0; i < bildMatrix.rows(); i++) {
            for (int j = 0; j < bildMatrix.cols(); j++) {
                double[] pixel = bildMatrix.get(i, j);
                int value = (int) pixel[0];
                int newValue = (int) lut[value];
                ausgabe.put(i, j, newValue);
                System.out.println("ausgabe[" + i + "][" + j + "]=" + newValue);
            }
        }
        return ausgabe;
    }

}