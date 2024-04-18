import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Bild {
    // Attribute
    public Mat bildMatrix;

    // Konstruktor
    public Bild(String dateiName) {
        bildMatrix = Imgcodecs.imread(dateiName, Imgcodecs.IMREAD_UNCHANGED);
        if (bildMatrix.empty()) {
            System.err.println("nicht gefunden: " + dateiName);
            System.exit(0);
        }
    }

    // Konstruktor überladen
    public Bild(Mat matrix) {
        bildMatrix = matrix;
    }

    // Methoden
    public void speichereIn(String dateiName) {
        Imgcodecs.imwrite(dateiName, bildMatrix);
    }

    public void show(String name) {
        HighGui.imshow(name, bildMatrix);
        HighGui.namedWindow(name, HighGui.WINDOW_AUTOSIZE);
    }

    public void wartung(int zeit) {
        HighGui.waitKey(zeit);
        System.exit(0);
    }

    public void groesseAendern(double faktor) {
        Size size = new Size(bildMatrix.width() * faktor, bildMatrix.height() * faktor);
        Imgproc.resize(bildMatrix, bildMatrix, size);
    }

    public void spaltenFarbeAendern(int n, int m, double[] farbe) {
        if (n < 0 || m > bildMatrix.cols()) {
            System.err.println("ungueltiger Bereich");
            return;
        }
        for (int zeile = 0; zeile < bildMatrix.rows(); zeile++) {
            for (int spalte = n; spalte < m; spalte++) {
                bildMatrix.put(zeile, spalte, farbe);
            }
        }
    }

    public void showHistogramm(String titel, int zeit, int kanal) {
        Histogramm histogramm = new Histogramm();
        histogramm.generiere(bildMatrix);
        histogramm.showMittelWertUndAbweichung(bildMatrix, kanal);
        HighGui.imshow(titel, histogramm.histogrammMatrix);
        HighGui.waitKey(zeit);
        System.exit(0);
    }
}
