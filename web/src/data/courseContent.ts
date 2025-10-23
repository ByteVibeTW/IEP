// 課程內容資料
export const courseContentMap: Record<string, any> = {
  '什麼是 React 鉤子以及為什麼要使用它們？': {
    type: 'doc',
    content: `
# 什麼是 React 鉤子以及為什麼要使用它們？

React Hooks 是 React 的革命性補充，可讓您在功能元件中使用狀態和其他 React 功能。在 Hooks 之前，這些功能是類別元件獨有的。了解引入 Hook 的原因以及它們解決的問題對於理解它們在現代 React 開發中的重要性至關重要。鑑於您對 React 元件的熟悉程度，我們將重點放在 Hook 提供的具體優勢，以及為什麼它們成為如此受歡迎的選擇。

# Hooks 解決的問題

鉤子解決了與類別元件相關的幾個限制和複雜性：

## 1.重用狀態邏輯的困難

類別元件最重要的挑戰之一是難以在元件之間重複使用有狀態邏輯。在 Hooks 之前，共享有狀態邏輯的主要機制是高階元件 （HOC） 和渲染道具。

- 高階元件 （HOC）：HOC 包裝元件以新增額外的 props 或行為。雖然它們允許代碼重複使用，但它們通常會導致"包裝器地獄"，即多個嵌套組件的 HOC 使組件樹難以理解和調試。它們還引入了命名空間衝突，因為 HOC 可能會傳遞名稱與包裝元件已經使用的名稱衝突的 props。

例： 想像一下，需要將身份驗證邏輯新增至多個元件。HOC 可以處理此問題，但由於新增的層，驗證 HOC 包裝的每個元件都變得更難檢查和理解。

- Render Props：Render props 使用函數作為元件的子項來共享有狀態邏輯。雖然它們避免了 HOC 的一些巢狀問題，但它們可能會使元件結構冗長且更難閱讀。

例： 提供滑鼠位置作為狀態的元件可以使用渲染道具。這意味著任何需要滑鼠位置的元件都將具有直接子項的功能，可能會破壞元件的自然結構。

鉤子提供了一種更乾淨、更直接的方式來透過自訂鉤子重複使用有狀態邏輯。我們將在後面的模組中介紹自訂鉤子。

## 2. 複雜的元件結構和生命週期方法

類別元件通常會變得龐大且複雜，尤其是在處理多種副作用（資料取得、訂閱、手動 DOM 操作）時。相關副作用的邏輯通常分散在不同的生命週期方法中，例如 componentDidMount、componentDidUpdate 和 componentWillUnmount。這使得很難理解程式碼的哪些部分是相關的，並可能導致錯誤。

- 分散邏輯： 考慮一個獲取資料、設定訂閱並手動更新 DOM 的元件。這些動作的程式碼可能分散在多個生命週期方法中，因此很難看到它們之間的關聯。
- 生命週期方法混淆： 使用生命週期方法時很容易出錯。例如，在 componentWillUnmount 中忘記取消訂閱可能會導致記憶體洩漏。

Hooks，特別是 useEffect，可讓您將相關邏輯分組在一起，使元件更易於閱讀和維護。

## 3. 類別元件語法和 this 關鍵字

類別元件使用 this 關鍵字，由於其動態綁定，這可能會讓 JavaScript 開發人員感到困惑。您經常需要在建構函式中綁定事件處理常式，以確保這引用正確的實例。這會新增樣板程式碼，並可能成為錯誤的來源。
-  this 綁定： 忘記繫結事件處理常式會導致執行階段錯誤。

\`\`\`javascript
class MyComponent extends React.Component {
  constructor(props) {
    super(props);
    this.handleClick = this.handleClick.bind(this); // Binding required
  }

  handleClick() {
    console.log(this); // Without binding, 'this' will be undefined
  }

  render() {
    return <button onClick={this.handleClick}>Click Me</button>;
  }
}
\`\`\`

- 樣板代碼： 建構函式和繫結步驟會將不必要的程式碼新增至簡單的元件。

帶有 Hooks 的功能元件完全避免了這些問題，因為它們不使用它並且通常更簡潔。

## 4. 熱重載困難

在某些情況下，類別元件的變更在開發期間不會可靠地熱重新載入，因此需要重新整理整個頁面。這減慢了開發過程。掛鉤作為功能，往往與熱重載系統整合得更好。

# 使用 React Hooks 的優點

鑑於這些問題，Hooks 提供了幾個關鍵優勢：

##  1. 程式碼可重複使用

如前所述，Hook 提供了一種乾淨的機制，用於使用自訂 Hook 在元件之間重複使用有狀態邏輯。

例： 您可以建立名為 useFetch 的自訂 Hook 來處理資料擷取邏輯。任何需要取得資料的元件都可以使用這個 Hook，大大減少了程式碼的重複。

## 2. 程式碼更簡單、可讀性更強

鉤子促進編寫更小、更集中的函數，從而產生更具可讀性和可維護性的元件。

例： 您可以將相關程式碼分組在 useEffect Hook 中，而不是將邏輯分散在生命週期方法中。

## 3. 函數式程式設計範式

鉤子鼓勵函數式程式設計風格，從而產生更可預測和可測試的程式碼。

例： 透過使用 Hooks，您可以編寫更易於推理和測試的純函數。

## 4. 不再使用 this 關鍵字

具有 Hook 的功能元件消除了對 this 關鍵字的需求，從而簡化了程式碼並減少了潛在的錯誤。

例： 事件處理常式可以直接在函式內定義，而不需要繫結此專案 

## 5. 更好的熱重載

Hooks 通常與熱重載配合得更好，從而改善了開發體驗。

# 簡單比較：類別與帶有鉤子的功能元件

為了說明差異，讓我們看看一個簡單的計數器元件，它既實作為類別元件，又實作為具有 Hooks 的功能元件。

\`\`\`javascript
import React from 'react';

class Counter extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      count: 0
    };
    this.increment = this.increment.bind(this); // Binding is necessary
  }

  increment() {
    this.setState({ count: this.state.count + 1 });
  }

  render() {
    return (
      <div>
        <p>Count: {this.state.count}</p>
        <button onClick={this.increment}>Increment</button>
      </div>
    );
  }
}

export default Counter;
\`\`\`

帶掛鉤的功能組件：

\`\`\`javascript
import React, { useState } from 'react';

function Counter() {
  const [count, setCount] = useState(0); // Using the useState Hook

  return (
    <div>
      <p>Count: {count}</p>
      <button onClick={() => setCount(count + 1)}>Increment</button>
    </div>
  );
}

export default Counter;
\`\`\`

如您所見，帶有 Hooks 的功能元件更簡潔、更易於閱讀。useState Hook 處理狀態管理，而不需要 this 或生命週期方法。

# 練習：將簡單類別元件轉換為具有鉤點的功能元件

使用 useState Hook 將下列類別元件轉換為功能元件：

\`\`\`javascript
import React from 'react';

class Greeting extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      name: 'Guest'
    };
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange(event) {
    this.setState({ name: event.target.value });
  }

  render() {
    return (
      <div>
        <label>Enter your name: </label>
        <input type="text" value={this.state.name} onChange={this.handleChange} />
        <p>Hello, {this.state.name}!</p>
      </div>
    );
  }
}

export default Greeting;
\`\`\`

Solution: 

\`\`\`javascript
import React, { useState } from 'react';

function Greeting() {
  const [name, setName] = useState('Guest');

  const handleChange = (event) => {
    setName(event.target.value);
  };

  return (
    <div>
      <label>Enter your name: </label>
      <input type="text" value={name} onChange={handleChange} />
      <p>Hello, {name}!</p>
    </div>
  );
}

export default Greeting;
\`\`\`

# 結論和後續步驟

React Hooks 提供了一種更有效率、可讀且可重複使用的方式來管理功能元件中的狀態和副作用。它們解決了類別元件的幾個限制，並鼓勵更實用的程式設計風格。鑑於您之前的 React 經驗，您現在已經做好了充分的準備，可以在即將推出的模組中更深入地研究特定的 Hook，例如 useState 和 useEffect。
      `
  },
  '功能元件與類別元件：回顧': {
    type: 'doc',
    content: `
# 功能元件與類別元件：回顧

功能元件已成為編寫 React 元件的標準方式，特別是隨著 React Hooks 的引入。然而，類別元件多年來一直是傳統方法，了解它們的差異對於維護現有程式碼庫和掌握 React 的演進至關重要。本課程全面回顧了函數式元件和類別元件，強調了它們的主要區別，並幫助您在接下來的課程中利用功能式元件中 React Hook 的強大功能。因為您提到您對元件有基本的了解，所以我們將重點放在函數式元件和類別元件之間的主要區別，而不是元件如何運作的基礎知識。

# 功能元件和類別元件之間的主要差異

以下是核心差異的細分，特別是在狀態管理和生命週期方法方面。

## 1. Syntax and Structure

- 功能元件： 這些是 JavaScript 函數，可選擇接受 props 作為參數並傳回 React 元素。它們簡潔明了，專注於基於 props 渲染 UI。

\`\`\`javascript
function Welcome(props) {
  return <h1>Hello, {props.name}</h1>;
}
// Or using arrow function syntax:
const Welcome = (props) => <h1>Hello, {props.name}</h1>;
\`\`\`

- 類別元件： 這些是擴展 React.Component 的 ES6 類別。它們必須具有傳回 React 元素的 render（） 方法。

\`\`\`javascript
class Welcome extends React.Component {
  render() {
    return <h1>Hello, {this.props.name}</h1>;
  }
}
\`\`\`

## 2. 狀態管理

- 功能元件（鉤子之前）： 從歷史上看，功能元件是無狀態的。這意味著他們無法管理自己的狀態。他們完全依賴從父元件傳遞下來的道具
- 類別元件： 類別元件使用 this.state 管理狀態，this.state 是包含元件狀態變數的物件。您可以在建構函式中初始化狀態並使用 this.setState（） 對其進行更新。

\`\`\`javascript
class Counter extends React.Component {
  constructor(props) {
    super(props);
    this.state = { count: 0 };
    this.handleClick = this.handleClick.bind(this); // Binding is important!
  }

  handleClick() {
    this.setState({ count: this.state.count + 1 });
  }

  render() {
    return (
      <div>
        <p>Count: {this.state.count}</p>
        <button onClick={this.handleClick}>Increment</button>
      </div>
    );
  }
}
\`\`\`

重要提示：this.setState（） 是非同步的 。React 可能會將多個 setState 呼叫批次處理在一起以提高效能。此外 ，您必須使用 this.setState（） 來更新狀態。直接修改 this.state 不會觸發重新渲染，並且可能導致不可預測的行為。此外，您必須在建構函式中將函數綁定到 this。如果沒有綁定， 這將在 handleClick 方法中未定義。另一種（也是更現代的）方法是使用箭頭函數，它會自動綁定它 ：

\`\`\`javascript
class Counter extends React.Component {
  constructor(props) {
    super(props);
    this.state = { count: 0 };
  }

  handleClick = () => {
    this.setState({ count: this.state.count + 1 });
  }

  render() {
    return (
      <div>
        <p>Count: {this.state.count}</p>
        <button onClick={this.handleClick}>Increment</button>
      </div>
    );
  }
}
\`\`\`

- 功能元件（帶鉤子）：React 16.8 中引入的 React Hooks 徹底改變了功能元件。useState Hook 允許功能元件管理狀態。我們將在下一個模組中深入探討這一點。

## 3. 生命週期方法

- 功能元件（鉤子之前）： 功能元件是無狀態的，沒有生命週期方法。他們只是根據收到的 props 進行渲染。
- 類別元件： 類別元件具有生命週期方法，可讓您在元件存在的特定點執行操作（掛載、更新、卸載）。最常用的生命週期方法有：

componentDidMount（）：在元件掛載（插入 DOM）後立即呼叫。非常適合執行初始資料取得、設定訂閱或直接操作 DOM。

componentDidUpdate（prevProps， prevState）： 在更新發生後立即呼叫。您可以使用它來執行副作用以回應 prop 或狀態變更。 要： 您應該根據對 prevProps 或 prevState 的變更有條件地執行副作用，以防止無限循環。

componentWillUnmount（）：在元件被卸載和銷毀之前立即呼叫。對於清理計時器、訂閱或事件偵聽器等資源以防止記憶體洩漏至關重要。

\`\`\`javascript
class Example extends React.Component {
  constructor(props) {
    super(props);
    this.state = { data: null };
  }

  componentDidMount() {
    // Example: Fetch data when the component mounts
    fetch('https://api.example.com/data')
      .then(response => response.json())
      .then(data => this.setState({ data: data }));
  }

  componentDidUpdate(prevProps, prevState) {
    // Example: Update something when the data state changes
    if (this.state.data !== prevState.data) {
      console.log('Data has been updated!');
    }
  }

  componentWillUnmount() {
    // Example: Clean up any subscriptions or timers
    console.log('Component will unmount');
  }

  render() {
    return (
      <div>
        {this.state.data ? <p>Data: {this.state.data.value}</p> : <p>Loading...</p>}
      </div>
    );
  }
}
\`\`\`

功能元件（帶鉤子）：useEffect Hook 提供了一種在功能元件中執行副作用的方法，有效地取代了生命週期方法。我們將在後面的模組中深入探討這一點。


## 4.  this 關鍵字

- 類別元件： 在類別元件中，this 關鍵字指的是元件實例。在類別中使用方法時，您需要注意正確綁定這一點 ，尤其是在將它們作為回呼傳遞時。
- 功能組件： 功能組件不使用它。 它們在自己的範圍內運行，因此不太容易出現 this 綁定問題。


## 5. 可讀性和簡潔性

- 功能元件： 通常被認為更具可讀性和簡潔性，特別是與 Hooks 結合使用時。它們提倡更實用的程式設計風格。
- 類別元件： 可能會變得更加冗長，特別是對於具有重要狀態管理和生命週期邏輯的複雜元件。

## 實戰實例

讓我們用一個簡單的例子來說明這些差異：一個顯示當前時間並每秒更新一次的元件。

類別組件：

\`\`\`javascript
class Clock extends React.Component {
  constructor(props) {
    super(props);
    this.state = { date: new Date() };
  }

  componentDidMount() {
    this.timerID = setInterval(
      () => this.tick(),
      1000
    );
  }

  componentWillUnmount() {
    clearInterval(this.timerID);
  }

  tick() {
    this.setState({
      date: new Date()
    });
  }

  render() {
    return (
      <div>
        <h1>Hello, world!</h1>
        <h2>It is {this.state.date.toLocaleTimeString()}.</h2>
      </div>
    );
  }
}
\`\`\`

解釋：

- Clock 類別擴展了 React.Component。
- 建構函式會以目前日期初始化狀態。
- componentDidMount 設定一個每秒呼叫 this.tick（） 的計時器。
- componentWillUnmount 會清除計時器以防止記憶體流失。
- tick（） 會使用新日期更新狀態，觸發重新渲染。

對等功能元件 （使用勾點 - 預覽）：

\`\`\`javascript
import React, { useState, useEffect } from 'react';

function Clock() {
  const [date, setDate] = useState(new Date());

  useEffect(() => {
    const timerID = setInterval(() => {
      setDate(new Date());
    }, 1000);

    return () => {
      clearInterval(timerID);
    };
  }, []); // Empty dependency array ensures this runs only on mount and unmount

  return (
    <div>
      <h1>Hello, world!</h1>
      <h2>It is {date.toLocaleTimeString()}.</h2>
    </div>
  );
}
\`\`\`

解釋（簡短 - 稍後會詳細介紹）：

- 我們從 react 匯入 useState 和 useEffect。
- useState 會建立一個狀態變數 date 和一個函數 setDate 來更新它。
- useEffect 設定計時器並在元件卸載時清除它（返回函數）。
- 空的相依性陣列 [] 告訴 useEffect 只執行一次，類似於 componentDidMount 和 componentWillUnmount。我們將在未來的模組中進一步探索相依性。

# 習題
- Conversion: 採用您之前編寫的簡單類別元件（或在網路上找到一個簡單的範例），並使用 useState 將其重寫為函數式元件（即使您還沒有完全理解它 - 這是一個很好的預覽！專注於基本結構以及如何以不同的方式管理狀態。
- 生命週期實驗： 在類別元件中，在 componentDidMount、componentDidUpdate 和 componentWillUnmount 中新增 console.log 語句。渲染和卸載元件（例如，根據 prop 有條件地渲染它）。觀察主控台中呼叫生命週期方法的順序。如何使用 React Hooks 在功能元件中實現類似的結果？

# 摘要和後續步驟

在本課中，我們回顧了 React 中函數式元件和類別元件之間的主要區別，重點關注語法、狀態管理和生命週期方法。雖然類別元件歷來用於有狀態邏輯和生命週期管理，但 React Hook 現在使功能元件能夠更有效地處理這些任務。

在下一課中，我們將設定一個專門配置用於使用 React Hooks 的開發環境。這將涉及確保您安裝了正確版本的 Node.js、npm（或 yarn）和 React，以及配置合適的程式碼編輯器以及必要的擴展，以獲得流暢的開發體驗。
      `
  }
};

export default courseContentMap;

