/*
 * Name: Shaunak Divine
 * EID: jsd2672
 */


//import java.util.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Your solution goes in this class.
 *
 * Please do not modify the other files we have provided for you, as we will use
 * our own versions of those files when grading your project. You are
 * responsible for ensuring that your solution works with the original version
 * of all the other files we have provided for you.
 *
 * That said, please feel free to add additional files and classes to your
 * solution, as you see fit. We will use ALL of your additional files when
 * grading your solution. However, do not add extra import statements to this file.
 */
public class Program1 extends AbstractProgram1 {

    /**
     * Determines whether a candidate Matching represents a solution to the stable matching problem.
     * Study the description of a Matching in the project documentation to help you with this.
     */
    @Override
    public boolean isStableMatching(Matching problem) {
        /* TODO implement this function */

        //first make shallow copy of university pref to then create inverse pref list
        ArrayList<ArrayList<Integer>> inverseUniversityPref = new ArrayList<>();
        ArrayList<ArrayList<Integer>> inverseStudentPref = new ArrayList<>();

        for (int i = 0; i < problem.getUniversityCount(); i++) {
            inverseUniversityPref.add(i, (ArrayList<Integer>) problem.getUniversityPreference().get(i).clone());
        }

        for (int i = 0; i < problem.getStudentCount(); i++) {
            inverseStudentPref.add(i, (ArrayList<Integer>) problem.getStudentPreference().get(i).clone());
        }

        //inverses the pref list
        for (int x = 0; x < problem.getUniversityCount(); x++) {
            for (int y = 0; y < problem.getStudentCount(); y++) {
                inverseUniversityPref.get(x).set(problem.getUniversityPreference().get(x).get(y), y);
            }
        }

        for (int x = 0; x < problem.getStudentCount(); x++) {
            for (int y = 0; y < problem.getUniversityCount(); y++) {
                inverseStudentPref.get(x).set(problem.getStudentPreference().get(x).get(y), y);
            }
        }

        for (int i = 0; i < problem.getStudentCount(); i++) {
            int currentUniv = problem.getStudentMatching().get(i);  //returns a university index of match
            if (currentUniv == -1){
                continue;
            }
            for (int r = 0; r < problem.getUniversityPreference().get(currentUniv).size(); r++){
                int studPrime = problem.getUniversityPreference().get(currentUniv).get(r);
                int univPrime = problem.getStudentMatching().get(studPrime);
                if (univPrime == -1){
                    return false;
                }
                if (inverseStudentPref.get(studPrime).get(currentUniv) < inverseStudentPref.get(studPrime).get(univPrime)){
                    return false;
                }
                if (studPrime == i){
                    break;
                }
            }
        }
        return true;

    }

    /**
     * Determines a solution to the stable matching problem from the given input set. Study the
     * project description to understand the variables which represent the input to your solution.
     *
     * @return A stable Matching.
     */
    @Override
    public Matching stableMatchingGaleShapley_universityoptimal(Matching problem) {
        /* TODO implement this function */

        ArrayList<ArrayList<Integer>> inverseUniversityPref = new ArrayList<>();
        ArrayList<ArrayList<Integer>> inverseStudentPref = new ArrayList<>();


        for (int i = 0; i < problem.getUniversityCount(); i++) {
            inverseUniversityPref.add(i, (ArrayList<Integer>) problem.getUniversityPreference().get(i).clone());
        }

        for (int i = 0; i < problem.getStudentCount(); i++) {
            inverseStudentPref.add(i, (ArrayList<Integer>) problem.getStudentPreference().get(i).clone());
        }

        //inverses the pref list
        for (int x = 0; x < problem.getUniversityCount(); x++) {
            for (int y = 0; y < problem.getStudentCount(); y++) {
                inverseUniversityPref.get(x).set(problem.getUniversityPreference().get(x).get(y), y);
            }
        }

        for (int x = 0; x < problem.getStudentCount(); x++) {
            for (int y = 0; y < problem.getUniversityCount(); y++) {
                inverseStudentPref.get(x).set(problem.getStudentPreference().get(x).get(y), y);
            }
        }

        LinkedList<Integer> openJobs = new LinkedList<Integer>();
        ArrayList<Integer> univPositionCopy = problem.getUniversityPositions();
        ArrayList<Integer> offerNumbers = new ArrayList<>();
        LinkedList<Integer> openStudents = new LinkedList<Integer>();
        ArrayList<Integer> finalMatching = new ArrayList<>();

        for (int i = 0; i < problem.getUniversityCount(); i++){
            openJobs.add(i);
            offerNumbers.add(i, 0);
        }

        for (int i = 0; i < problem.getStudentCount(); i++){
            openStudents.add(i);
            finalMatching.add(i, -1);
        }


        while(!openJobs.isEmpty()){
            int currUniv = openJobs.peek();
            int topStud = problem.getUniversityPreference().get(currUniv).get(offerNumbers.get(currUniv));
            offerNumbers.set(currUniv, offerNumbers.get(currUniv)+1);
            if (openStudents.contains(topStud)){
                finalMatching.set(topStud, currUniv);
                univPositionCopy.set(currUniv, univPositionCopy.get(currUniv) - 1);
                openStudents.remove(openStudents.indexOf(topStud));   //changed
                if (univPositionCopy.get(currUniv) == 0){
                    openJobs.remove(openJobs.indexOf(currUniv)); //changed
                }
            }
            else {
                int univPrime = finalMatching.get(topStud);
                if (inverseStudentPref.get(topStud).get(univPrime) < inverseStudentPref.get(topStud).get(currUniv)){
                    continue; // u remains free
                }
                else {
                    finalMatching.set(topStud, currUniv);
                    if (!openJobs.contains(univPrime)){
                        openJobs.addLast(univPrime);        //changed

                    }
                    univPositionCopy.set(univPrime, univPositionCopy.get(univPrime) + 1);
                    univPositionCopy.set(currUniv, univPositionCopy.get(currUniv) - 1);
                    if (univPositionCopy.get(currUniv) == 0){
                        openJobs.remove(openJobs.indexOf(currUniv));
                    }
                }
            }
        }

        problem.setStudentMatching(finalMatching);

        return problem;
    }

    /**
     * Determines a solution to the stable matching problem from the given input set. Study the
     * project description to understand the variables which represent the input to your solution.
     *
     * @return A stable Matching.
     */
    @Override
    public Matching stableMatchingGaleShapley_studentoptimal(Matching problem) {
        /* TODO implement this function */

        ArrayList<ArrayList<Integer>> inverseUniversityPref = new ArrayList<>();
        ArrayList<ArrayList<Integer>> inverseStudentPref = new ArrayList<>();


        for (int i = 0; i < problem.getUniversityCount(); i++) {
            inverseUniversityPref.add(i, (ArrayList<Integer>) problem.getUniversityPreference().get(i).clone());
        }

        for (int i = 0; i < problem.getStudentCount(); i++) {
            inverseStudentPref.add(i, (ArrayList<Integer>) problem.getStudentPreference().get(i).clone());
        }

        //inverses the pref list
        for (int x = 0; x < problem.getUniversityCount(); x++) {
            for (int y = 0; y < problem.getStudentCount(); y++) {
                inverseUniversityPref.get(x).set(problem.getUniversityPreference().get(x).get(y), y);
            }
        }

        for (int x = 0; x < problem.getStudentCount(); x++) {
            for (int y = 0; y < problem.getUniversityCount(); y++) {
                inverseStudentPref.get(x).set(problem.getStudentPreference().get(x).get(y), y);
            }
        }

        LinkedList<Integer> openJobs = new LinkedList<Integer>();
        ArrayList<Integer> univPositionCopy = problem.getUniversityPositions();
        ArrayList<Integer> offerNumbers = new ArrayList<>();
        LinkedList<Integer> openStudents = new LinkedList<Integer>();
        ArrayList<Integer> finalMatching = new ArrayList<>();



        for (int i = 0; i < problem.getUniversityCount(); i++){
            openJobs.add(i);

        }

        for (int i = 0; i < problem.getStudentCount(); i++){
            openStudents.add(i);
            offerNumbers.add(i, 0);
            finalMatching.add(i, -1);
        }


        while(!openStudents.isEmpty()){
            int currStud = openStudents.peek();
            if (offerNumbers.get(currStud) >= problem.getUniversityCount()) {
                openStudents.remove(openStudents.indexOf(currStud));    //changed
                continue;
            }
            int topUniv = problem.getStudentPreference().get(currStud).get(offerNumbers.get(currStud));
            offerNumbers.set(currStud, offerNumbers.get(currStud)+1);
            if (openJobs.contains(topUniv)){
                finalMatching.set(currStud, topUniv);
                univPositionCopy.set(topUniv, univPositionCopy.get(topUniv) - 1);
                openStudents.remove(openStudents.indexOf(currStud));    //changed
                if (univPositionCopy.get(topUniv) == 0){
                    openJobs.remove(openJobs.indexOf(topUniv)); //changed
                }
            }
            else {
                int studPrime = finalMatching.indexOf(topUniv);
                int tempLowestRank = -1;
                for (int i = 0; i < finalMatching.size(); i++){
                    if (finalMatching.get(i) == topUniv){
                        if (inverseUniversityPref.get(topUniv).get(i) > tempLowestRank){
                            tempLowestRank = inverseUniversityPref.get(topUniv).get(i);
                            studPrime = i;
                        }
                    }
                }

                if (inverseUniversityPref.get(topUniv).get(studPrime) < inverseUniversityPref.get(topUniv).get(currStud)){
                    continue; // u remains free
                }
                else {
                    finalMatching.set(currStud, topUniv);
                    if (!openStudents.contains(studPrime)){
                        openStudents.addLast(studPrime);
                    }
                    openStudents.remove(openStudents.indexOf(currStud));
                    finalMatching.set(studPrime, -1);
//                    }
                }
            }
        }

        problem.setStudentMatching(finalMatching);

        return problem;
    }
    }

