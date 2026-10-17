-- Data here will be inserted into the database when the server starts.
INSERT INTO Advisor (Name, Email, Phone, Password)
    VALUES ('Jane Smith', 'jane.smith@example.com', '(555)555-5556', 'password456');
INSERT INTO Client (Name, Advisor_ID, Email, Phone, Password)
    VALUES ('John Doe', 1, 'john.doe@example.com', '(555)555-5555', 'password123');
INSERT INTO Scenario (Client_ID, Name, Description)
    VALUES (1, 'Retirement Plan', 'John Doe needs to John Retire.');
INSERT INTO Financial_Asset (Scenario_ID, Name, Description, APR, Mortgage, Interest)/*Value,*/ 
    VALUES (1, 'House', 'House on Smith Street', '3.5', '300000', '5.0');/* '500000',*/