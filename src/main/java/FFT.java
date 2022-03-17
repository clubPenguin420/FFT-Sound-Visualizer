import java.io.IOException;
import java.io.FileWriter;

public class FFT {
    static double max = 0.0;
    public static void main(String[] args) throws IllegalArgumentException, IOException {

        double period = 2*Math.PI;
        Complex[] data = generate(4096, 0, period);
        FileWriter write = new FileWriter("inputData.txt");
        int n = 4;
        Complex[] x = new Complex[n];

        // original data
        for (int i = 0; i < n; i++) {
            x[i] = new Complex(i+1, i+1);
        }
        for (Complex num : data) {
//            System.out.println(num);
            write.write(num.toString() + "\n");
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
            data[x] = new Complex((4/Math.PI)*(Math.sin(i) + Math.sin(3 * i)/3 + Math.sin(5 * i)/5 + Math.sin(7 * i)/7), 0);
        }
        return data;
    }

    public static Complex[] fft(Complex[] data) throws IllegalArgumentException {
        int n = data.length;
        if(n == 1) {
            return new Complex[]{data[0]};
        }
        else if(n % 2 != 0) {
            throw new IllegalArgumentException("Not a power of 2 you fat cunt");
        }

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

//    public static Complex[] fft(Complex[] x) {
//        int n = x.length;
//
//        // base case
//        if (n == 1) return new Complex[] { x[0] };
//
//        // radix 2 Cooley-Tukey FFT
//        if (n % 2 != 0) {
//            throw new IllegalArgumentException("n is not a power of 2");
//        }
//
//        // compute FFT of even terms
//        Complex[] even = new Complex[n/2];
//        for (int k = 0; k < n/2; k++) {
//            even[k] = x[2*k];
//        }
//        Complex[] evenFFT = fft(even);
//
//        // compute FFT of odd terms
//        Complex[] odd  = even;  // reuse the array (to avoid n log n space)
//        for (int k = 0; k < n/2; k++) {
//            odd[k] = x[2*k + 1];
//        }
//        Complex[] oddFFT = fft(odd);
//
//        // combine
//        Complex[] y = new Complex[n];
//        for (int k = 0; k < n/2; k++) {
//            double kth = -2 * k * Math.PI / n;
//            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
//            y[k]       = evenFFT[k].plus (wk.times(oddFFT[k]));
//            y[k + n/2] = evenFFT[k].minus(wk.times(oddFFT[k]));
//        }
//        return y;
//    }
}
