import java.net.Socket;

public class ServerDriver{
    public static void main(String[] args){
        ServerUI ui = new ServerUI();
        String[] testStrings = new String[]{
                "Acrobats stab orca",
                "poor guy dump",
                "23454032",
                "A man, a plan, a cat, a ham, a yak, a yam, a hat, a canal-Panama",
                "As I pee, sir, I see Pisa",
                "Air an aria.",
                "123454321",
                "Was it a car or a cat I saw"
        };

        for(int i =0; i < testStrings.length;i++){
            System.out.println(checkPalindrome(testStrings[i], true));
        }
    }

    public static Boolean checkPalindrome(String in, Boolean verbose){
        //Sanitize
        in = in.replaceAll("\\W", "");
        in = in.toLowerCase();

        //Convert to char arrays
        char[] inAsChar = in.toCharArray();
        char[] tmp = new char[inAsChar.length];

        for(int i=in.length()-1, j=0; i>=0; i--, j++){
            tmp[j] = inAsChar[i];
        }
        //Verbose logging
        if(verbose){
            System.out.println("Original: " + new String(inAsChar) +
                             "\nReversed: " + new String(tmp) +
                             "\nEqual?    " + java.util.Arrays.equals(inAsChar, tmp));
        }
            return java.util.Arrays.equals(inAsChar, tmp);
    }
    public static Boolean checkPalindrome(String in){ return checkPalindrome(in, false); }
}

