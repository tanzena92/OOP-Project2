
package src;

import java.io.*;
import java.util.*;

public class BotTrust {
    public static void main(String args[]) throws FileNotFoundException, IOException{

        String filename = "src\\small.input.txt";
        FileInputStream fstream_in = new FileInputStream(filename);
        DataInputStream in = new DataInputStream(fstream_in);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        FileWriter fstream = new FileWriter("out.txt");
        PrintWriter output = new PrintWriter(fstream);

        int n_test = Integer.parseInt(br.readLine());
        for(int i_test=0; i_test<n_test; i_test++){
            String line_input = br.readLine();
            String[] word_input = line_input.split(" ");
            int n_buttons = Integer.parseInt(word_input[0]);

            ArrayList<String> next_stop = new ArrayList<String>();
            ArrayList<Integer> next_time_stop = new ArrayList<Integer>();
            ArrayList<Integer> next_dist_O = new ArrayList<Integer>();
            ArrayList<Integer> next_dist_B = new ArrayList<Integer>();

            int counter = 1;
            for(int i=0; i<n_buttons; i++){
                
                int next_time = Integer.parseInt(word_input[counter+1]);
                String next_person = word_input[counter];

                next_stop.add(next_person);
                next_time_stop.add(next_time);

                if(next_person.equals("O")){
                    next_dist_O.add(next_time);
                } else {
                    next_dist_B.add(next_time);
                }
                
                counter += 2;
            }

            HashMap<String, Integer> curr_pos = new HashMap<String, Integer>();
            curr_pos.put("O", 1);
            curr_pos.put("B", 1);
            int curr_time = 0;

            //System.out.println("O's stops : " + next_dist_O.toString());
            //System.out.println("B's stops : " + next_dist_B.toString());

            int initial_size = next_time_stop.size();
            for(int iter=0; iter<initial_size; iter++){
                int next_time = next_time_stop.get(iter);
                String next_person = next_stop.get(iter);
//                System.out.println("Checking for :" + next_time + ":" + next_person);
                
                
                int curr_pos_O = curr_pos.get("O");
                int curr_pos_B = curr_pos.get("B");

                if(next_dist_O.isEmpty()){
                    //calculate time for B to complete the sequence
//                    System.out.println("Here");
//                    System.out.println("POS O :" + curr_pos_O);
//                    System.out.println("POS B :" + curr_pos_B);
                    while(!next_dist_B.isEmpty()){
                        int dist = Math.abs(curr_pos_B - next_dist_B.get(0));
                        curr_pos_B = next_dist_B.get(0);
//                        System.out.println("Distance to move : " + dist);
                        next_dist_B.remove(0);
                        curr_time += dist;
                        curr_time += 1;
                    }
                    break;
                }

                if(next_dist_B.isEmpty()){
//                    System.out.println("There");
//                    System.out.println("POS O :" + curr_pos_O);
//                    System.out.println("POS B :" + curr_pos_B);
                    //calculate time for B to complete the sequence
                    while(!next_dist_O.isEmpty()){
                        int dist = Math.abs(curr_pos_O - next_dist_O.get(0));
//                        System.out.println("Distance to move : " + dist);
                        curr_pos_O = next_dist_O.get(0);
                        next_dist_O.remove(0);
                        curr_time += dist;
                        curr_time += 1;
                    }
                    break;
                }

                int dist_O = Math.abs(next_dist_O.get(0) - curr_pos_O);
                int dist_B = Math.abs(next_dist_B.get(0) - curr_pos_B);

                if(next_person.equals("O")){
//                    System.out.println("POS O :" + curr_pos_O);
//                    System.out.println("POS B :" + curr_pos_B);
                    if(dist_O < dist_B){
                        curr_time += dist_O;
                        curr_pos.put("O", next_dist_O.get(0));
                        next_dist_O.remove(0);


                        //change the position of B
                        if(curr_pos_B < next_dist_B.get(0)){
                            curr_pos_B += dist_O;
                            curr_pos_B += 1;
                            curr_pos.put("B", curr_pos_B);
                        } else {
                            curr_pos_B -= dist_O;
                            curr_pos_B -= 1;
                            curr_pos.put("B", curr_pos_B);
                        }

                        curr_time += 1; //to push the switch
                        continue;
                    } else{
                        curr_time += dist_O;
                        curr_pos.put("O", next_dist_O.get(0));
                        next_dist_O.remove(0);


                        //change the position of B
                        curr_pos_B = next_dist_B.get(0);
                        curr_pos.put("B", curr_pos_B);

                        curr_time += 1; //to push the switch
                        continue;
                    }
                }

                if(next_person.equals("B")){
//                    System.out.println("POS O :" + curr_pos_O);
//                    System.out.println("POS B :" + curr_pos_B);
                    if(dist_B < dist_O){
                        curr_time += dist_B;
                        curr_pos.put("B", next_dist_B.get(0));
                        next_dist_B.remove(0);

                        //change the position of O
                        if(curr_pos_O < next_dist_O.get(0)){
                            curr_pos_O += dist_B;
                            curr_pos_O += 1;
                            curr_pos.put("O", curr_pos_O);
                        } else {
                            curr_pos_O -= dist_B;
                            curr_pos_O -= 1;
                            curr_pos.put("O", curr_pos_O);
                        }

                        curr_time += 1; //to push the switch
                        continue;
                    } else{
//                        System.out.println("Comes here.");
                        curr_time += dist_B;
                        curr_pos_B = next_dist_B.get(0);
                        curr_pos.put("B", curr_pos_B);
//                        System.out.println("New posn : " + next_dist_B.get(0));
                        next_dist_B.remove(0);

                        //change the position of O
                        curr_pos_O = next_dist_O.get(0);
                        curr_pos.put("O", curr_pos_O);

                        curr_time += 1; //to push the switch
                        continue;
                    }
                }
            }
            System.out.println("Case #" + (i_test + 1) + ": " + curr_time);
            
            String txt = "Case #" + (i_test + 1) + ": ";
            output.print(txt);
            output.println(curr_time);
        }
        output.close();
    }
}