import junit.framework.TestCase;

import ntnu.stud.markul.crowdshelf.NetworkController;

public class NetworkHelperTest extends TestCase {

    public void testSendPostRequest() throws Exception {
        /*
        {"_id":"55e6dcfff3f1beaf21673c8e","name":"The best crowd","owner":"esso","members":["esso","torstein"]}
         */

        //String jsonString = {"_id":"55e6dcfff3f1beaf21673c8e","name":"The best crowd","owner":"esso","members":["esso","torstein"]}
        String crowdID = "55e6dcfff3f1beaf21673c8e";
        NetworkController.getCrowd(crowdID);
    }

    public void testSendPutRequest() throws Exception {

    }

    public void testSendGetRequest() throws Exception {

    }

    public void testHandleJsonResponse() throws Exception {

    }
}