# ParityCheckApplication
This application calculates valid codewords for a given parity check matrix and can provide useful information about the associated code

### Features
<ul>
    <li>Compute Generator Matrix</li>
    <li>Generate all valid codewords</li>
    <li>Calculate Hamming Distance</li>
    <li>Give insights into the code's properties</li>
</ul>

### How to Use
There are two main ways to use this application.

<ul>
    <li>Run it in your favorite IDE: Navigate to Application.java and run the main method. You can either pass
    your arguments in args[] or you can edit the hard-coded values at the beginning of the main method. If no arguments can be 
    parsed from args[], the hard-coded values will be used for the calculations.</li>
    <li>Open the .jar-file from the command line: After navigating to the root directory, you can write <code>java -jar
    Parity-Check-Matrix-1.2.jar</code> to run the application. The parity-check-matrix and a limit for possible values
    can be added to the command by simply attaching them to the end of the command.</li>
</ul>
The application has two arguments that can be passed: the parity-check-matrix and the limit. The parity-check-matrix 
should be given as a string, following either Latex or Wolfram Alpha syntax. The limit describes
the upper bound of possible values in the words, codewords and the parity-check-matrix. Oftentimes those values can
only be in F_2={0,1}; in that case limit = 2. If no limit is specified, limit=2 will be used.

#### Example inputs

<ul>
 <li><code>java -jar Parity-Check-Matrix-1.2.jar</code> This runs the application with the hard-coded values as parameters</li>
 <li><code>java -jar Parity-Check-Matrix-1.2.jar "{{1,1,1,1,1}}"</code> This runs the application with the parity-check-matrix
    {{1,1,1,1,1}} and the standard limit of 2</li>
 <li><code>java -jar Parity-Check-Matrix-1.2.jar "{{1,1,1,1,1}}" 4</code> This runs the application with the parity-check-matrix
    {{1,1,1,1,1}} and a limit of 4 which means 0, 1, 2 and 3 are possible values (eg. in a codeword)</li>
 <li><code>java -jar Parity-Check-Matrix-1.2.jar "\begin{pmatrix} 1&1&1&1&1 \end{pmatrix}" 4</code> This works just like
    the previous example but uses Latex syntax instead of Wolfram Alpha syntax for the matrix</li>
 <li><code>java -jar Parity-Check-Matrix-1.2.jar solveWholeExam</code> This will probably not work, but I'm not sure as I haven't tried it yet!</li> 
</ul>
