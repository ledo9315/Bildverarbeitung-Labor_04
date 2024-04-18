import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class HauptProg {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        aufgaben_04_2();
    }
    public static void aufgaben_04_2() {

        Mat eingabe = new Mat (6,5,CvType.CV_16UC1);
        eingabe.put(0,0,gibData());

        new Bild(eingabe).speichereIn("Original.jpg");

        Histogramm hist = new Histogramm();
        int[] histogramm = hist.gibHistogramm(eingabe);
        float[] lut = hist.gibNormalisierteLUT(histogramm, eingabe.width() * eingabe.height());

        Mat ausgabe = hist.gibAequalisierteMatrix(lut, eingabe);
        Bild aequalisiert = new Bild(ausgabe);
        aequalisiert.showHistogramm("Histogramm Aequalisiert",10000, 0);
        new Bild(ausgabe).speichereIn("Aequalisiert.jpg");
    }
    public static short[] gibData() {
        return new short[]{102, 102, 105, 104, 104,
                103, 101, 101, 101, 103,
                105, 101, 107, 101, 105,
                104, 101, 101, 101, 104,
                102, 101, 106, 101, 102,
                106, 104, 108, 105, 108};
    }
}