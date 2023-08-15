from flask import Flask, request, jsonify
import csv
import mysql.connector

app = Flask(__name__)

@app.route('/get_data', methods=['GET'])
def get_data():
    try:
        cnx = mysql.connector.connect(
            user='coldvl',
            password='qwertyP1',
            host='hv305868-001.ca.clouddb.ovh.net',
            database='workograph-db-test',
            port=35967
        )

        cursor = cnx.cursor()

        query = "SELECT * FROM your_table_name"
        cursor.execute(query)
        results = cursor.fetchall()

        cursor.close()
        cnx.close()

        data = [{"column1": row[0], "column2": row[1]} for row in results]
        return jsonify(data)

    except mysql.connector.Error as err:
        return jsonify({"error": str(err)}), 500

@app.route('/export_csv', methods=['GET'])
def export_csv():
    # Similar CSV export code as before
    # ...

 if __name__ == '__main__':
    app.run(debug=True)
