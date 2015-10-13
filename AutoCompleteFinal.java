package Homework;
import java.util.Collection;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import Homework.Trie;
import java.util.Queue;
import java.util.Scanner;

public class AutoCompleteFinal<T> implements IAutocomplete<T> {
	
	Scanner debugger = new Scanner(System.in);
	Trie<T> everything = new Trie<T>();
	ArrayList<String> MasterList = new ArrayList<String>();
	Queue<TrieNode<T>> holderList = new ArrayDeque<TrieNode<T>>();
	StringBuilder Word = new StringBuilder("");
	StringBuilder Word1 = new StringBuilder("");
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
	@SuppressWarnings("unchecked")
	public void pickCandidate(String prefix, String candidate) 
	{
		TrieNode<T> prefixNode = everything.find(prefix.trim());
		if(prefixNode == null)
		{
			everything.put(prefix.trim(), null);
			prefixNode = everything.find(prefix.trim());
			prefixNode.setValue((T) new ArrayList<String>());
			prefixNode.setEndState(false);
		}
		ArrayList<String> tempList = (ArrayList<String>) prefixNode.getValue();
		TrieNode<T> candidateNode = everything.find(candidate.trim());
		if(candidateNode == null)
		{
			everything.put(candidate.trim(), null);
			candidateNode = everything.find(candidate.trim());
			candidateNode.setEndState(true);
			if(checkIfRepeat(candidate.trim(), tempList) == false) tempList.add(candidate.trim());	
		} else
		{
			if(checkIfRepeat(candidate.trim(), tempList) == false) tempList.add(candidate.trim());
		}
	}
	
	@Override
	public T put(String key, T value)
	{	
		return everything.put(key, value);
	}
	
	@SuppressWarnings("unchecked")
	public void createCandidateList(TrieNode<T> thisNode, String initialPrefix)
	{
		if(thisNode!=null)
		{
		if(thisNode.getValue() == null) thisNode.setValue((T) new ArrayList<String>());
		ArrayList<String> pickedList = (ArrayList<String>) thisNode.getValue();
		for(int i =pickedList.size()-1; i>=0; i--) if (pickedList.get(i) != null) MasterList.add(pickedList.get(i).trim());
		holderList.add(thisNode);
		bfs();
		thisNode.setValue((T) pickedList);
		} else System.out.println("That prefix does not exist, but I went ahead and added it in!"); 
	}
	
	public void bfs()
	{
		while(!holderList.isEmpty() && MasterList.size() < 20)
		{
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
				endStateWord(currentNode);
			}	
		}
	}
	
	public void endStateWord(TrieNode<T> currentNode)
	{
		Word1.append(finalWord(currentNode));
		String temp = Word1.reverse().toString();
		if (checkIfRepeat(temp.trim(), MasterList) == false) MasterList.add(temp.trim());
		Word1.delete(0, Word1.length());
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
	
	public Boolean checkIfRepeat(String s, ArrayList<String> x)
	{
		if(x.contains(s.trim())) return true;
		else return false;
		
				
	}
}

