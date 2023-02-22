# Solid Security Research
## About this repo
The following is a github repository for open source research that I completed during my NLP research internship at a startup called Solid Security (a now defunct company)

Within each of the folders are a readme detailing how to execute and use said code.  The code and methods should be documented well enough to understand, but for a high level overview of the project, please refer to the [paper](https://github.com/SolidSecurity/Solid-Spintax-Specification) I wrote detailing the system.

My role in this project was to do white paper write-ups, coding the project, expand on the concept, and researching NLP methods to make it feasible, while under minimal guidance of Solid Security.

## The concept
![image](https://user-images.githubusercontent.com/26073390/220499153-06e80e7d-bfd1-4fca-9a93-ca8fa719613b.png)
The concept behind this project, in summary is to have a leak mitigation system where text can be protected from copying by making the text itself uniquely identifiable, through creating indexable linguistic permutations that retain the meaning.  Through the process of a "red black tree", the pair choices made will narrow down the individual approaching statistically significant confidence over the course of a document, even if the user attempts to paraphrase.  A step by step explanation can be seen in the presentation shown [here](https://github.com/Fuehnix/Solid-Security-Research/blob/master/Papers%20and%20Presentations/Presentation.pdf)

## Future work
In this project, I was unable to complete work on the "Inspector" component before the research was shut down due to COVID conflicts. This component would have auto-generated spintax rules based on linguistic understanding about what is okay to change.  However, to achieve such a process *well* is actually part of an unsolved problem in NLP, computational pragmatics.  With the innovations of transformers and GPT, I think the same approach that is being taken towards "cheat detection" with ChatGPT could also be modified towards this system as well.

However, my research interests are more related towards Cognitive NLP, computational social sciences, multimodal NLP, and novel approaches that work around the large LLM craze.  I want to work on more "organic" NLP, and NLP that seems more human.
