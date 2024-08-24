### 

I do not want to print entire API response but only specific part of that response. 
e.g. response.data.input.text in column1 and all the response.data.meanings.meaning in column2. How to do that
```
{
  "header": {
    "url": "http://localhost:8080/sa/dictionary/words?letter=A",
    "requestTime": "2024-08-02T09:42:27.304+05:30",
    "responseTime": "2024-08-02T09:42:27.365+05:30",
    "elapsedTimeInMillis": 61,
    "httpStatus": 200
  },
  "response": {
    "multi": true,
    "key": "words",
    "count": 1,
    "data": [
      {
        "input": {
          "language": "ENGLISH",
          "text": "A"
        },
        "meanings": [
          {
            "language": "ENGLISH",
            "meaning": "A"
          },
          {
            "language": "HINGLISH",
            "meaning": "seb"
          }
        ]
      }
    ]
  }
}
```

This can be achieved using the below code.
```

import React, { useState, useEffect} from 'react';

const Example = () => {
    const [wordsData, setWordsData] = useState([]);
    useEffect(() => {
        const fetchWords = async () => {
            try {
                const token = localStorage.getItem("token");
                const response = await fetch(`http://localhost:8080/sa/dictionary/words?letter=${encodeURIComponent('A')}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    }
                });
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                const data = await response.json();
                setWordsData(data.response.data);
            } catch (error) {
                console.log(error);
            } finally {
                console.log('ok')
            }
        };
        fetchWords();
    }, []);

    return (
            <div>
                <table>
                    <thead>
                        <tr>
                            <th>Word</th>
                            <th>Meanings</th>
                        </tr>
                    </thead>
                    <tbody>
                        {wordsData.map((wordEntry, index) => (
                            <tr key={index}>
                                <td>{wordEntry.input.text}</td>
                                <td>
                                    {wordEntry.meanings.map((meaning, mIndex) => (
                                        <div key={mIndex}>{meaning.meaning}</div>
                                    ))}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
    );
};

export default Example;
```