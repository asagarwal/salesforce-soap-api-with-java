import com.sforce.soap.enterprise.Connector;
import com.sforce.soap.enterprise.DeleteResult;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.Error;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.sobject.Lead;
import com.sforce.ws.ConnectorConfig;
import com.sforce.ws.ConnectionException;
 
public class Main {
 
    static final String USERNAME = "username@salesforce.com";
    static final String PASSWORD = "passwordSecurityToken";
    static EnterpriseConnection connection;
 
    public static void main(String[] args) {
 
        ConnectorConfig config = new ConnectorConfig();
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
 
        try {
              connection = Connector.newConnection(config);
 
               // display some current settings
              System.out.println("Auth EndPoint: "+config.getAuthEndpoint());
              System.out.println("Service EndPoint: "+config.getServiceEndpoint());
              System.out.println("Username: "+config.getUsername());
              System.out.println("SessionId: "+config.getSessionId());
 
              // run the different examples
             queryLeads();                   // Query Leads from Salesforce
             createLeads();                  // Create Leads in Salesforce
             updateLeads();                  // Update Leads in Salesforce
             deleteLeads();                  // Delete Leads in Salesforce
 
        } catch (ConnectionException e1) {
            e1.printStackTrace();
        }  
 
        try {
             connection.logout();
              System.out.println("Logged out.");
       } catch (ConnectionException ce) {
                  ce.printStackTrace();
       }
    }     
 
    // queries and displays the 5 newest leads
    private static void queryLeads() {
 
      System.out.println("Querying for the 5 newest Leads...");
 
      try {
 
        // query for the 5 newest Leads
        QueryResult queryResults = connection.query("SELECT Id, FirstName, LastName, Company FROM Lead ORDER BY CreatedDate DESC LIMIT 5");
        if (queryResults.getSize() > 0) {
          for (int i=0;i < 5;i++){
            // cast the SObject to a strongly-typed Lead
            Lead l = (Lead)queryResults.getRecords()[i];
            System.out.println("Id: " + l.getId() + " - Name: "+ l.getFirstName()+" "+ l.getLastName()+" - Company: "+l.getCompany());
          }
        }
 
      } catch (Exception e) {
        e.printStackTrace();
      }    
 
    }
 
    // create 5 test Leads
    private static void createLeads() {
 
      System.out.println("Creating 5 new test Leads...");
      Lead[] records = new Lead[5];
 
      try {
 
        // create 5 test leads
        for (int i=0;i < 5;i++) {
          Lead l = new Lead();
          l.setFirstName("SOAP API");
          l.setLastName("Lead "+i);
          l.setCompany("asagarwal.com");
 
          records[i] = l;
        }
 
        // create the records in Salesforce.com
        SaveResult[] saveResults = connection.create(records);
 
        // check the returned results for any errors
        for (int i=0; i <  saveResults.length; i++) {
          if (saveResults[i].isSuccess()) {
            System.out.println(i+". Successfully created record - Id: " + saveResults[i].getId());
          } else {
            Error[] errors = saveResults[i].getErrors();
            for (int j=0; j <  errors.length; j++) { System.out.println("ERROR creating record: " + errors[j].getMessage()); } } } } catch (Exception e) { e.printStackTrace(); } } // updates the 5 newly created Leads private static void updateLeads() { System.out.println("Update the 5 new test leads..."); Lead[] records = new Lead[5]; try { QueryResult queryResults = connection.query("SELECT Id, FirstName, LastName, Company FROM Lead ORDER BY CreatedDate DESC LIMIT 5"); if (queryResults.getSize() > 0) {
          for (int i=0;i < 5;i++){
            // cast the SObject to a strongly-typed Lead
            Lead l = (Lead)queryResults.getRecords()[i];
            System.out.println("Updating Id: " + l.getId() + " - Name: "+l.getFirstName()+" "+l.getLastName());
            // modify the name of the Lead
            l.setLastName(l.getLastName()+" -- UPDATED");
            records[i] = l;
          }
        }
 
        // update the records in Salesforce.com
        SaveResult[] saveResults = connection.update(records);
 
        // check the returned results for any errors
        for (int i=0; i <  saveResults.length; i++) {
          if (saveResults[i].isSuccess()) {
            System.out.println(i+". Successfully updated record - Id: " + saveResults[i].getId());
          } else {
            Error[] errors = saveResults[i].getErrors();
            for (int j=0; j <  errors.length; j++) { System.out.println("ERROR updating record: " + errors[j].getMessage()); } } } } catch (Exception e) { e.printStackTrace(); } } // delete the 2 newly created Leads private static void deleteLeads() { System.out.println("Deleting the 2 new test Leads..."); String[] ids = new String[2]; try { QueryResult queryResults = connection.query("SELECT Id, Name FROM Lead ORDER BY CreatedDate DESC LIMIT 2"); if (queryResults.getSize() > 0) {
          for (int i=0;i < queryResults.getRecords().length;i++) {
            // cast the SObject to a strongly-typed Lead
            Lead l = (Lead)queryResults.getRecords()[i];
            // add the Lead Id to the array to be deleted
            ids[i] = l.getId();
            System.out.println("Deleting Id: " + l.getId() + " - Name: "+l.getFirstName()+" "+l.getLastName());
          }
        }
 
        // delete the records in Salesforce.com by passing an array of Ids
        DeleteResult[] deleteResults = connection.delete(ids);
 
        // check the results for any errors
        for (int i=0; i <  deleteResults.length; i++) {
          if (deleteResults[i].isSuccess()) {
            System.out.println(i+". Successfully deleted record - Id: " + deleteResults[i].getId());
          } else {
            Error[] errors = deleteResults[i].getErrors();
            for (int j=0; j <  errors.length; j++) {
              System.out.println("ERROR deleting record: " + errors[j].getMessage());
            }
          }
        }
 
      } catch (Exception e) {
        e.printStackTrace();
      }    
 
    }
 
  }
