import java.io.IOException;
import java.io.FileWriter;

public class FFT {
    static double max = 0.0;
    public static void main(String[] args) throws IllegalArgumentException {

        double period = 2*Math.PI;
        Complex[] data = generate(4096, 0, period);

        int n = 4;
        Complex[] x = new Complex[n];

        // original data
        for (int i = 0; i < n; i++) {
            x[i] = new Complex(i+1, i+1);
        }
        System.out.println("Num of samples: " + data.length);
        Complex[] freq = fft(data);
//        double i = 0;
        for (int i = 0; i < freq.length / 2; ++i) {
            if(freq[i].abs() > max * 0.1)
                System.out.println("Frequency " + ((i*2*Math.PI)/period)  + ": Magnitude(" + freq[i].abs() + "), Phase(" + freq[i].phase() + ")");
        }
    }

    public static Complex[] generate(int terms, double lb, double ub) {
        Complex[] data = new Complex[terms];
        double range = ub - lb;
        double i = 0;
        for(int x = 0; x < terms; i += range/terms, x++) {
            data[x] = new Complex((4/Math.PI)*(Math.sin(i) + Math.sin(3*i)/3 + Math.sin(5*i)/5 + Math.sin(7*i)/7) + Math.sin(9*i)/9 + Math.sin(11*i)/11, 0);
        }
        return data;
    }

    public static Complex[] fft(Complex[] data) throws IllegalArgumentException {
        int n = data.length;
        if(n == 1) {
            return new Complex[]{data[0]};
        }
//        else if(n % 2 == 0) {
//            throw new IllegalArgumentException("Not a power of 2 you fat cunt");
//        }

        Complex[] evenSeq = new Complex[n / 2];
        for(int i = 0; i < evenSeq.length; ++i) {
            evenSeq[i] = data[2 * i];
        }
        Complex[] evenFFT = fft(evenSeq);

        Complex[] oddSeq = evenSeq;
        for(int i = 0; i < oddSeq.length; ++i) {
            oddSeq[i] = data[2 * i + 1];
        }
        Complex[] oddFFT = fft(oddSeq);

        Complex[] freq = new Complex[n];
        for(int k = 0; k < n / 2; ++k) {
            double c = (-2 * Math.PI * k) / n;
            Complex wk = new Complex(Math.cos(c), Math.sin(c));
            freq[k]       = evenFFT[k].plus(wk.times(oddFFT[k]));
            freq[k + n/2] = evenFFT[k].minus(wk.times(oddFFT[k]));
            double m = Math.max(freq[k].abs(), freq[k + n / 2].abs());
            if(m > max) {
                max = m;
            }
        }
        return freq;
    }

}
