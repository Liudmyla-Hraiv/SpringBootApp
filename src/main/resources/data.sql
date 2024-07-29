INSERT INTO time_slots (time_slot_date,start_time,end_time,is_keynote_time_slot)
VALUES ('2020-04-09','9:00','9:45',TRUE),
       ('2020-04-09','10:00','11:00',FALSE),
       ('2020-04-09','11:15','11:45',FALSE),
       ('2020-04-09','12:45','13:45',FALSE),
       ('2020-04-09','14:00','15:00',FALSE),
       ('2020-04-09','15:15','15:45',FALSE),
       ('2020-04-09','16:00','17:00',FALSE),
       ('2020-04-10','9:00','10:00',FALSE),
       ('2020-04-10','10:15','11:15',FALSE),
       ('2020-04-10','11:30','12:00',FALSE),
       ('2020-04-10','13:00','14:00',FALSE),
       ('2020-04-10','14:15','15:00',TRUE);

INSERT INTO sessions (session_name,session_length,session_description)
VALUES ('Keynote - The Golden Age of Software',45,''),
       ('A Better Way to Access Data with Spring Data',60,''),
       ('A Deep Dive Into Spring IoC',60,''),
       ('Building RESTful APIs with Spring Data Rest',60,''),
       ('Spring Integration Quick Start',60,''),
       ('Building Microservices with Spring',60,''),
       ('Spring Cloud Primer',60,''),
       ('Spring Boot in 30 Minutes',30,''),
       ('Testing Spring Applications',30,''),
       ('Writing Your First Advice with Spring AOP',30,''),
       ('IntelliJ IDEA Tips and Tricks',30,''),
       ('Functional Programming in Java',60,''),
       ('Making the Switch from Java to Kotlin',60,''),
       ('Mastering Concurrency in Java',60,''),
       ('Mastering Object-Orientated Programming in Java',60,''),
       ('SOLID Principles in Java',60,''),
       ('The Most Important Java Design Patterns',60,''),
       ('Using EasyMock for Java Testing',30,''),
       ('IntelliJ IDEA Debugging Secrets',30,''),
       ('Getting Started with Java Flight Recorder',30,''),
       ('Clean Code Principls for Java Developers',30,''),
       ('Designing Large Scale ASP.NET Core Applications',60,''),
       ('Application Security in ASP.NET Core Apps',60,''),
       ('.NET''s HttpClient: The Missing Manual',60,''),
       ('Using Vue.js in ASP.NET and ASP.NET Core Applications',60,''),
       ('Clean Code Practices in C#',60,''),
       ('Modern Desktop Applications in .NET',60,''),
       ('SignalR Quickstart',30,''),
       ('Visual Studio Tips and Tricks',30,''),
       ('Logging in ASP.NET Core',30,''),
       ('A Quick Tour of MiniProfiler',30,''),
       ('Demystifying Dependency Injection in .NET',60,''),
       ('Microservices Architectures using ASP.NET Core',60,''),
       ('Advanced Techniques in Entity Framework Core',60,''),
       ('C# Language Features You May Not Know About',60,''),
       ('Asynchronous Programming in .NET',60,''),
       ('Advanced Entity Framework Core',60,''),
       ('A lap around the .NET Command Line Interface (CLI)',30,''),
       ('Deploying .NET Apps with Azure DevOps',30,''),
       ('Code Analysis for .NET Projects',30,''),
       ('Better Logging with Serilog',30,''),
       ('Deploying Web Applications to Azure',60,''),
       ('Getting Started with Azure Cognitive Services',60,''),
       ('Building Microservices with Azure Functions',60,''),
       ('Big Data and Analytics on the Azure Platform',60,''),
       ('Getting Started with CosmosDB',60,''),
       ('Securing Azure Services',60,''),
       ('Azure Event Grid Quickstart',30,''),
       ('Managing Azure with the Azure CLI',30,''),
       ('Migrating to Azure SQL',30,''),
       ('Understanding the Azure Blockchain Service',30,''),
       ('Building Hybrid Cloud Architectures in AWS',60,''),
       ('Migrating On-Premises Applications to AWS',60,''),
       ('IOT Solutions Using AWS',60,''),
       ('Getting Startedd with Machine Learning in AWS',60,''),
       ('DevOps on the AWS Platform',60,''),
       ('Serverless Computing on AWS',60,''),
       ('Amazon DynamoDB Quickstart',30,''),
       ('Understanding the Amazon Elastic Kubernetes Service',30,''),
       ('Creating Your First Data Lake in AWS',30,''),
       ('Migrating to Amazon Aurora',30,''),
       ('How Agile Are You Really?',60,''),
       ('Better Retrospectives',60,''),
       ('Developer to Leader',60,''),
       ('Selling Your Ideas to Leadership: A Guide for Technology Professionals',60,''),
       ('Creating a Culture of Learning',60,''),
       ('The Seven Habits of Highly Effective Developers',60,''),
       ('Writing Better User Stories',30,''),
       ('Techniques for Better Estimates',30,''),
       ('Communication Skills for the Technology Professional',30,''),
       ('Personal Kanban',30,'');

INSERT INTO session_schedule (time_slot_id,session_id,room)
VALUES (1,1,'Grand Ballroom'),
       (2,2,'Cedar'),
       (4,3,'Cedar'),
       (5,4,'Cedar'),
       (7,5,'Cedar'),
       (8,6,'Cedar'),
       (11,7,'Cedar'),
       (3,8,'Cedar'),
       (6,9,'Cedar'),
       (9,10,'Cedar'),
       (10,11,'Cedar'),
       (2,12,'Cherry'),
       (4,13,'Cherry'),
       (5,14,'Cherry'),
       (7,15,'Cherry'),
       (8,16,'Cherry'),
       (11,17,'Cherry'),
       (3,18,'Cherry'),
       (6,19,'Cherry'),
       (9,20,'Cherry'),
       (10,21,'Cherry'),
       (2,22,'Maple'),
       (4,23,'Maple'),
       (5,24,'Maple'),
       (7,25,'Maple'),
       (8,26,'Maple'),
       (11,27,'Maple'),
       (3,28,'Maple'),
       (6,29,'Maple'),
       (9,30,'Maple'),
       (10,31,'Maple'),
       (2,32,'Aspen'),
       (4,33,'Aspen'),
       (5,34,'Aspen'),
       (7,35,'Aspen'),
       (8,36,'Aspen'),
       (11,37,'Aspen'),
       (3,38,'Aspen'),
       (6,39,'Aspen'),
       (9,40,'Aspen'),
       (10,41,'Aspen'),
       (2,42,'Hickory'),
       (4,43,'Hickory'),
       (5,44,'Hickory'),
       (7,45,'Hickory'),
       (8,46,'Hickory'),
       (11,47,'Hickory'),
       (3,48,'Hickory'),
       (6,49,'Hickory'),
       (9,50,'Hickory'),
       (10,51,'Hickory'),
       (2,52,'Cottonwood'),
       (4,53,'Cottonwood'),
       (5,54,'Cottonwood'),
       (7,55,'Cottonwood'),
       (8,56,'Cottonwood'),
       (11,57,'Cottonwood'),
       (3,58,'Cottonwood'),
       (6,59,'Cottonwood'),
       (9,60,'Cottonwood'),
       (10,61,'Cottonwood'),
       (2,62,'Sycamore'),
       (4,63,'Sycamore'),
       (5,64,'Sycamore'),
       (7,65,'Sycamore'),
       (8,66,'Sycamore'),
       (11,67,'Sycamore'),
       (3,68,'Sycamore'),
       (6,69,'Sycamore'),
       (9,70,'Sycamore'),
       (10,71,'Sycamore');


INSERT INTO speakers (first_name,last_name,title,company,speaker_bio,speaker_photo)
VALUES ('Sergio','Becker','Senior Developer','MicroOcean Software','Test', null),
       ('James','Lowrey','Solutions Architect','Fabrikam Industries','Test', null),
       ('Gloria','Franklin','Enerprise Architect','Carved Rock Online','Test', null),
       ('Lori','Vanhoose','Java Technical Lead','National Bank','Test', null),
       ('Raymond','Hall','Senior Developer','City Power and Electric','Test', null),
       ('Sam','Vasquez','Software Analyst','Globalmantics Consulting','Test', null),
       ('Justin','Clark','Principal Engineer','Tangerine Hippopotamus Consulting','Test', null),
       ('Barbara','Williams','Senior DBA','Contoso','Test', null),
       ('James','Sharp','Technical Lead','Adventureworks','Test', null),
       ('Julie','Rowe','Software Architect','Northwind Trading','Test', null),
       ('Tonya','Burke','Senior Cloud Consultant','Big Data Consulting','Test', null),
       ('Nicole','Perry','Engineering Manager','World Wide Importers','Test', null),
       ('James','Curtis','Cloud Architect','Farmington Research','Test', null),
       ('Patti','White','Program Manager','State Investments','Test', null),
       ('Andrew','Graham','Software Architect','Property Insurance Group','Test', null),
       ('Lenn','van der Brug','Solutions Architect','Globalmantics Consulting','Test', null),
       ('Stephan','Leijtens','Application Development Manager','Bank Europe','Test', null),
       ('Anja','Koehler','Software Engineer','Contoso','Test', null),
       ('Petra','Holtzmann','Senior API Engineer','European Investment Partners','Test', null),
       ('Jens','Lundberg','Consultant','Aqua Sky Consulting','Test', null),
       ('Linda','Carver','Senior Developer','Chicago Technology Research','Test', null),
       ('Ronald','McMillian','Software Architect','National Bank','Test', null),
       ('Dustin','Finn','Software Engineer','Globalmantics Consulting','Test', null),
       ('Sharon','Johnson','Solutions Architect','National Aerospace Technologies','Test', null),
       ('Karen','McClure','.NET Architect','Adventureworks','Test', null),
       ('Matthew','Thompson','Technical Lead','Fabrikam Industries','Test', null),
       ('Chris','Moore','Solutions Architect','World Wide Importers','Test', null),
       ('Ken','Perry','Software Engineer','International Industrial Works','Test', null),
       ('Christie','Fournier','Application Architect','National Software Services','Test', null),
       ('Jenny','Lee','Azure Cloud Architect','Prairie Cloud Solutions','Test', null),
       ('Alicia','Peng','Senior Cloud Consultant','Cloud Management Partners','Test', null),
       ('Page','Reid','Lead Azure Engineer','State Investments','Test', null),
       ('Anke','Holzman','Senior AWS Consultant','Cloud Management Partners','Test', null),
       ('Dylan','Wilkinson','Principal AWS Engineer','Cloud Native Labs','Test', null),
       ('Henry','Duke','Engineering Lead','Wired Brain Coffee','Test', null),
       ('Cynthia','Crandall','Senior Business Analyst','Wired Brain Coffee','Test', null),
       ('Clara','Dawson','Agile Coach','Agile Coaches Inc','Test', null),
       ('Ann','Martinez','Senior AWS Consultant','Western Consulting Services','Test', null),
       ('James','King','Staff AWS Engineer','Northern States Bank','Test', null),
       ('Simon','Williams','Chief Technology Officer','NorthernSoft Systems','Test', null);

INSERT INTO session_speakers (session_id,speaker_id)
VALUES (1,40),
       (2,4),
       (3,5),
       (4,1),
       (5,15),
       (6,20),
       (7,21),
       (8,1),
       (9,4),
       (10,20),
       (11,5),
       (12,7),
       (13,23),
       (14,24),
       (15,22),
       (16,21),
       (17,22),
       (18,23),
       (19,7),
       (20,24),
       (21,15),
       (22,2),
       (23,3),
       (24,19),
       (25,25),
       (26,26),
       (27,27),
       (28,25),
       (29,9),
       (30,27),
       (31,16),
       (32,9),
       (33,16),
       (34,28),
       (35,29),
       (36,26),
       (37,28),
       (38,19),
       (39,3),
       (40,2),
       (41,29),
       (42,13),
       (43,30),
       (44,32),
       (45,31),
       (46,8),
       (47,31),
       (48,32),
       (49,13),
       (50,8),
       (51,30),
       (52,34),
       (53,39),
       (54,38),
       (55,33),
       (56,34),
       (57,38),
       (58,33),
       (59,39),
       (60,33),
       (61,33),
       (62,37),
       (63,17),
       (64,17),
       (65,14),
       (66,36),
       (67,35),
       (68,36),
       (69,37),
       (70,14),
       (71,35);

