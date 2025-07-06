const translateText = async (text, targetLang = 'vi') => {
  const response = await fetch(
    `https://translation.googleapis.com/language/translate/v2?key=YOUR_API_KEY`,
    {
      method: 'POST',
      body: JSON.stringify({
        q: text,
        target: targetLang,
        format: 'text',
      }),
      headers: {
        'Content-Type': 'application/json',
      },
    }
  )
  const data = await response.json()
  return data.data.translations[0].translatedText
}

const TranslateAPI = {
  translateText,
}
export default TranslateAPI
