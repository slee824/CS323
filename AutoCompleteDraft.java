package Homework;
import java.util.Collection;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import Homework.Trie;
import java.util.Queue;
import java.util.Scanner;

public class LeeAutocomplete<T> implements IAutocomplete<T> {
	
	Scanner debugger = new Scanner(System.in);
	Trie<T> everything = new Trie<T>();
	List<String> MasterList = new ArrayList<String>();
	Queue<TrieNode<T>> holderList = new ArrayDeque<TrieNode<T>>();
	String[] PickedList = new String[20];
	StringBuilder Word = new StringBuilder("");
	StringBuilder Word1 = new StringBuilder("");
	int placekeeper = 0;
	String x = "";

	@Override
	public List<String> getCandidates(String prefix) 
	{
		MasterList.clear();
		holderList.clear();
		TrieNode<T> thisNode = everything.find(prefix);
		createCandidateList(thisNode, prefix);
		return MasterList;
	}

	@Override
	public void pickCandidate(String prefix, String candidate) 
	{
		TrieNode<T> thisNode = everything.find(candidate);
		if (thisNode != null) PickedList[placekeeper++] = candidate;
		else everything.put(candidate.trim(), null);
		if (thisNode.isEndState() == false) thisNode.setEndState(true);
		
	}
	@Override
	public T put(String key, T value) {	
		return everything.put(key, value);
	}
	
	public void createCandidateList(TrieNode<T> thisNode, String initialPrefix)
	{
		for(int i = PickedList.length-1; i>=0; i--) 
		{
			if (PickedList[i] != null) MasterList.add(" " +PickedList[i]);
		}
		holderList.add(thisNode);
		bfs();
	}
	
	public void bfs()
	{
		while(!holderList.isEmpty() && MasterList.size() < 20)
		{
			boolean truth = false;
			x = "";
			Word.delete(0, Word.length());
			TrieNode<T> currentNode = holderList.remove();
			if (currentNode.hasChildren())
			{
				Collection<TrieNode<T>> childrenList = currentNode.getChildrenMap().values();
				for(TrieNode<T> child : childrenList) holderList.add(child);
			}
			if (currentNode.isEndState())
			{
				Word1.append(finalWord(currentNode));
				String temp = Word1.reverse().toString();
				//debugger.nextLine();
				//System.out.println("Current Word: " +temp);
				for (int i = 0; i < MasterList.size()-1; i++)
				{	
					if (MasterList.get(i).trim().compareToIgnoreCase(temp.trim()) == 0){
						truth = true;
						break;
					}
					//debugger.nextLine();
					//System.out.println("---------------------\nMasterlist element:" +MasterList.get(i));
					//System.out.println("Word that is being compared: " +temp + "\nT or F = " + truth +  "\n----------------");
				}
				//debugger.nextLine();
				//for (int i = 0; i < MasterList.size()-1; i++) System.out.println("MasterList:"+MasterList.get(i));
				if(!truth) MasterList.add(temp);
				Word1.delete(0, Word1.length());
			}
			
		}
	}
	public String finalWord(TrieNode<T> node)
	{
		x = Word.append(node.getKey()).toString();
		if (node.getParent() == null) 
		{
			return x;
		}
		return finalWord(node.getParent());
	}
}

