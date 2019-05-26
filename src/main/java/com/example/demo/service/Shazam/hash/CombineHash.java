package com.example.demo.service.Shazam.hash;

import com.example.demo.service.Shazam.pcm.PCM16MonoData;
import com.example.demo.service.Shazam.pcm.PCM16MonoParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 生成声纹信息
 * 
 * —— 参考 https://github.com/winston-wen/Shazam/ 实现
 */
public class CombineHash {

    // dividing an int by an INT ?! Naive!
    private static final double scaling = FFT.WINDOW_SIZE / 44100.0;

    private static final int interval_num = 5;

    private static final int start_freq = 110; // The frequency of tone A1

    private static final int[][] freqRanges = new int[interval_num][2];

    static {
        for (int i = 0; i < interval_num; ++i) {
            freqRanges[i][0] = (int) Math.round((start_freq << i) * scaling);
            freqRanges[i][1] = (int) Math.round((start_freq << (i + 1)) * scaling);
        }
    }

    /**
     * The strongest frequencies for each frame
     */
    private ArrayList<int[]> strong_freqs = new ArrayList<>();
    private int id;

    /**
     * For songs about to add into DB
     * @param id
     */
    public CombineHash(int id) {
        this.id = id;
    }

    /**
     * Append a column of frequency peaks to the list of strongest freqs.
     * A frequency peak is a frequency value whose amplitude is the highest among
     * all frequencies in a frequency interval.
     *
     * @param freqDomain The frequency domain strong_freqs generated by FFT.
     */
    public void append(double[] freqDomain) {
        int[] freqPeaks = new int[interval_num];

        // The maximum amplitude and its frequency
        double max;
        int max_freq;

        // find the peak frequency in each interval
        for (int i = 0; i < interval_num; ++i) {
            max = 0;
            max_freq = freqRanges[i][0];
            for (int j = freqRanges[i][0]; j < freqRanges[i][1]; ++j) {
                if (freqDomain[j] > max) {
                    max = freqDomain[j];
                    max_freq = j;
                }
            }
            freqPeaks[i] = max_freq;
        }
        strong_freqs.add(freqPeaks);
    }

    /**
     * Cross hashing.
     */
    private ArrayList<Hash> combine() {
        if (strong_freqs.size() < 3)
            throw new RuntimeException("Too few frequency peaks");
        ArrayList<Hash> hashes = new ArrayList<>();
        for (int frame = 0; frame < strong_freqs.size() - 2; ++frame) {
            for (int idx_f1 = 0; idx_f1 < interval_num; ++idx_f1) {
                for (int dt = 1; dt <= 2; ++dt) {
                    for (int idx_f2 = 0; idx_f2 < interval_num; ++idx_f2) {
                        // do not combine with frequencies of the same octave
                        if (idx_f1 == idx_f2)
                            continue;
                        Hash hash = new Hash();
                        hash.f1 = strong_freqs.get(frame)[idx_f1];
                        hash.f2 = strong_freqs.get(frame+dt)[idx_f2];
                        hash.dt = dt;
                        hash.offset = frame;
                        hash.song_id = id;
                        hashes.add(hash);
                    }
                }
            }
        }
        return hashes;
    }
    
    public static ArrayList<Hash> generateFingerprint(File song, int id) throws IOException {
    
        // extract PCM data
        PCM16MonoData data = PCM16MonoParser.parse(song);
        CombineHash map = new CombineHash(id);
    
        for (int i = 0; i < data.getSampleNum(); ) {
            
            // collect FFT.WINDOW_SIZE number of samples.
            double[] time_dom = new double[FFT.WINDOW_SIZE];
            for (int j=0; i < data.getSampleNum() && j < FFT.WINDOW_SIZE; ++i, ++j) {
                time_dom[j] = data.getSample(i);
            }
        
            // call FFT to convert to frequency domain
            double[] freq_dom = FFT.fft(time_dom);
        
            // append a column of frequency peaks to the constellation map
            map.append(freq_dom);
        
            // hint gc to recycle this array.
            time_dom = null;
            freq_dom = null;
        }
    
        // generate combine hashes
        ArrayList<Hash> hashes = map.combine();
        data = null;
        map = null;
    
        return hashes;
    }
}
