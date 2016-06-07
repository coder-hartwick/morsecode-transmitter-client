# morsecode-transmitter-client
The client application that will connect to the morsecode-transmitter-server application.

Right now, this has been configured to work on localhost. If you know how to compile a java application and want to run the program across multiple computers, do the following:
<ol>
<li>Fork the morsecode-transmitter-client repository in NetBeans, Eclipse, or through the command line (I use NetBeans IDE).</li>
<li>Navigate to the Client.java file in the client package.</li>
<li>Search for the following code using CTRL+F:<br>
  <code style="background-color: #eff0f1;">private final String host = "localhost";</code><br>
</li>
<li>Where it says localhost, change it to the IP Address of the computer you will be running the server application on.</li>
<li>Compile and run the application after starting the server.</li>
</ol>


<h3>To run the program, perform the following steps:</h3>
<ol>
<li>Download the morsecode-transmitter-server.jar file from the main branch of the morsecode-transmitter-server repository.</li>
<li>Download the morsecode-transmitter-client.jar file from the transmitter-client-working branch of the morsecode-transmitter-client repository.</li>
<li>Open the command line and navigate to the folder where the files are saved.</li>
<li>Make sure java has permission to go through the firewall in Windows. A window asking you to allow this may appear after performing the next step. This is needed for server/client connectivity.</li>
<li>You must start the server first, so do the following:<br>
  <code style="background-color: #eff0f1;">java -jar morsecode-transmitter-server.jar</code><br>
</li>
<li>To start the client, do the following:<br>
  <code style="background-color: #eff0f1;">java -jar morsecode-transmitter-client.jar</code>
</li>
</ol>
