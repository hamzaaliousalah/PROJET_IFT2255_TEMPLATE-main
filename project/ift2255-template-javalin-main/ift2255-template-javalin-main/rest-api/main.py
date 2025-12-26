## jai suivi ce tuto :https://www.youtube.com/watch?v=d0o89z134CQ
from langchain_ollama import OllamaLLM
from langchain_core.prompts import ChatPromptTemplate


template = """
answer the question below .

here is the conversation history : {context}

Question: {question}

Answer:
"""

##run this fir : $ source chatbot/Scripts/activate pour l'environment
##basically on feed tout le temps le contxte de la convo au model pr pas faire de la recorrection et ajustement, et il va repondre a ce tempate de prompt

model=OllamaLLM(model="llama3.2:3b")
prompt = ChatPromptTemplate.from_template(template)
chain = prompt | model #retnrer le prompt dans le model, meme op que le pipeline de bash

def handle_conversation():
  context = ""
  print("welcome to chatbot")
  while True:
    user_input = input("You: ")
    if user_input.lower() == "exit":
      break
    result = chain.invoke({"context": context,"question":user_input})

    print("Bot: ", result)
    context+= f"\nUser: {user_input}\nAI: {result}"

#result = chain.invoke({"context": "","question":""})
if __name__ == "__main__":
  handle_conversation()