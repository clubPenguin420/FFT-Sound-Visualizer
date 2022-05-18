import java.io.IOException;
import java.io.FileWriter;

public class FFT {
    static double max = 0.0;
    public static void main(String[] args) throws IllegalArgumentException {

        double period = 2*Math.PI;
        Double[] data = generate(16384, 0, period);

        int n = 4;
        Double[] x = new Double[n];

        // original data
//        for (int i = 0; i < n; i++) {
//            x[i] = new Double(i+1, i+1);
//        }
        System.out.println("Num of samples: " + data.length);
        Double[] freq = fft(data);
////        double i = 0;
//        for (int i = 0; i < freq.length / 2; ++i) {
//            if(freq[i].abs() > max * 0.1)
//                System.out.println("Frequency " + ((i*2*Math.PI)/period)  + ": Magnitude(" + freq[i].abs() + "), Phase(" + freq[i].phase() + ")");
//        }
    }

    public static Double[] generate(int terms, double lb, double ub) {
        Double[] data = new Double[terms];
        double range = ub - lb;
        double i = 0;
        for(int x = 0; x < terms; i += range/terms, x++) {
            data[x] = (4/Math.PI)*(Math.sin(1000*i) + Math.sin(i) + Math.sin(3*i)/3 + Math.sin(5*i)/5 + Math.sin(7*i)/7) + Math.sin(9*i)/9 + Math.sin(11*i)/11;
        }
        return data;
    }

    public static Double[] fft(Double[] data) throws IllegalArgumentException {
        int n = data.length;
        if(n == 1) {
            return new Double[]{data[0]};
        }

        Double[] evenSeq = new Double[n / 2];
        for(int i = 0; i < evenSeq.length; ++i) {
            evenSeq[i] = data[2 * i];
        }
        Double[] evenFFT = fft(evenSeq);

        Double[] oddSeq = evenSeq;
        for(int i = 0; i < oddSeq.length; ++i) {
            oddSeq[i] = data[2 * i + 1];
        }
        Double[] oddFFT = fft(oddSeq);

        Double[] freq = new Double[n];
        for(int k = 0; k < n / 2; ++k) {
            double c = (-2 * Math.PI * k) / n;
            Double wk = Math.exp(c);
            freq[k]       = evenFFT[k] + wk * oddFFT[k];
            freq[k + n/2] = evenFFT[k] - wk * oddFFT[k];
            double m = Math.max(Math.abs(freq[k]), Math.abs(freq[k + n / 2]));
            if(m > max) {
                max = m;
            }
        }
        return freq;
    }

}
