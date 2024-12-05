from fastapi import FastAPI, HTTPException 
from pydantic import BaseModel
from sklearn.feature_extraction.text import TfidfVectorizer
import requests
import uvicorn

app = FastAPI()

NEWS_API_KEY = '3d3c3a25dd3743769ac9069407290e8a'  # Replace with your News API key
NEWS_API_URL = 'https://newsapi.org/v2/top-headlines'

class TextRequest(BaseModel):
    text: str

def extract_keywords(text, n=5):
    vectorizor = TfidfVectorizer(stop_words='english')
    Tfidf_matrix = vectorizor.fit_transform([text])
    keywords = vectorizor.get_feature_names_out()
    scores = Tfidf_matrix.toarray().flatten()
    sorted_indices = scores.argsort()[::-1]
    top_keywords = [keywords[i] for i  in sorted_indices[:n]]
    return top_keywords

@app.post("/extract_keywords")
async def keyword_extraction(request: TextRequest):
    text = request.text
    if not text.strip():
        raise HTTPException(status_code = 400,detail="No text provided")
    
    keywords = extract_keywords(text=text)
    return{"keywords": keywords}

if __name__ == "__main__":
    uvicorn.run(app)
