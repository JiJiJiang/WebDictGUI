/**
 * Created by 77 on 2016/12/14.
 */
import java.util.*;
import java.io.*;

public class ReadDictionary {
    public static ArrayList<String> wordOrPhraseArray=new ArrayList<String>();//单词（短语）

    //字典的读取器
    public static ArrayList<String> getDictionary()
    {
        return wordOrPhraseArray;
    }
    /*
     *  读取字典文件构建字典
     * 封装为public方法对外提供该接口
     * */
    public static void initialDictionary() throws Exception{
        // TODO Auto-generated constructor stub
        File dictionary=new File("dictionary/dictionary.txt");
        Scanner input=new Scanner(dictionary);
        wordOrPhraseArray.clear();
        while(input.hasNextLine())
        {
            String word=input.nextLine();
            //System.out.println(word);
            wordOrPhraseArray.add(word);
        }
        input.close();
        //Collections.sort(wordOrPhraseArray);
        //for(String str:wordOrPhraseArray)
            //System.out.println(str);
    }

    /*字典的二分查找匹配*/
    public static int binarySearch(String input) {
        int low = 0, high = wordOrPhraseArray.size() - 1;
        int mid=0;
        while (low <= high) {
            mid = (high + low) / 2;
            if (wordOrPhraseArray.get(mid).toLowerCase().compareTo(input.toLowerCase()) > 0)
                high = mid - 1;
            else if (wordOrPhraseArray.get(mid).toLowerCase().compareTo(input.toLowerCase()) < 0)
                low = mid + 1;
            else
                break;
            }
        if(low>high)
            return low;
        else
            return mid;
    }
}
