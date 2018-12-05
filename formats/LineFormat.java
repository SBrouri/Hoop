package formats;

import java.io.*;

public class LineFormat implements Format {

    protected String fname;
    protected OpenMode mode;
    protected int index;

    protected InputStream ins;
    protected InputStreamReader insReader;
    protected BufferedReader buffReader;

    protected FileWriter outs;
    protected BufferedWriter buffWriter;
    protected PrintWriter pWriter;


    @Override
    public KV read() {
        if (mode == OpenMode.R){
            index++;
            try {
                String line = buffReader.readLine();
                if (line != null) {
                    return new KV("", line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            
        }
        return null;
    }

    @Override
    public void write(KV record) {
        if (mode == OpenMode.W) {
            index++;
            this.pWriter.println(record.v);
            
        } else {
            
        }
    }

    @Override
    public void open(OpenMode mode) {
        if (fname != null) {
            this.mode = mode;
            this.index = 0;
            if (mode == OpenMode.R) {
                try {
                    this.ins = new FileInputStream(this.fname);
                    this.insReader = new InputStreamReader(this.ins);
                    this.buffReader = new BufferedReader(this.insReader);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    
                }
            } else {
                try {
                    this.outs = new FileWriter(this.fname);
                    this.buffWriter = new BufferedWriter(this.outs);
                    this.pWriter = new PrintWriter(this.buffWriter);
                } catch (IOException e) {
                    e.printStackTrace();
                   
                }
            }
        } else {
            //error
        }
    }

    @Override
    public void close() {
        try {
            if (mode == OpenMode.R) {
                buffReader.close();
                insReader.close();
                ins.close();
            } else if (mode == OpenMode.W) {
                pWriter.close();
                buffWriter.close();
                outs.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            
        }
        mode = null;
    }

    @Override
    public long getIndex() {
        return this.index; 
    }

    @Override
    public String getFname() {
        return fname;
    }

    @Override
    public void setFname(String fname) {
        this.fname = fname;
    }
}
