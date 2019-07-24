package br.com.santander.predictbacen.reclameaqui.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CsvUtil {
    public static void writeContentToFile( String filePath, ArrayList<String> fileContent )
    {
        File file = new File(filePath);
        FileWriter fr = null;
        try
        {
            // Below constructor argument decides whether to append or override
            fr = new FileWriter(file);

            for( String text : fileContent )
            {
                fr.write(text + System.lineSeparator());
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                fr.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }
}
