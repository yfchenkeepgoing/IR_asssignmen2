package ie.tcd.cs7is3;

import org.junit.jupiter.api.Test;
import ie.tcd.cs7is3.parser.FR94Parser;
import ie.tcd.cs7is3.parser.TopicsParser;
import ie.tcd.cs7is3.parser.FBISParser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

public class AppTest {

  // @Test
  // public void testFR94Object() {
  //   FR94Object fr94Object = new FR94Object();

  //   fr94Object.setTitle("aaaa");

  //   System.out.println(fr94Object.getTitle());
  // }

  // @Test
  // public void testFR94Parser() throws IOException {
  //   FR94Parser fr94Parser = new FR94Parser();

  //   fr94Parser.getFr94Documents();

  // }

  //   @Test
  // public void testFBISParser() throws IOException {
  //   FBISParser fbisParser = new FBISParser();

  //   fbisParser.getFBISDocuments();

  // }

    @Test
    public void testFBISParser() throws IOException {
      TopicsParser topicsParser = new TopicsParser();

      topicsParser.getTopicsDocs();

    }
}