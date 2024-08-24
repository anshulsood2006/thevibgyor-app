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
                console.log('ok');
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